package com.yunyou.common.utils.xml;

import com.yunyou.common.utils.xml.impl.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class XmlObjectUtils {
    private static Map<Class, Converter> converterMap;

    static {
        converterMap = new HashMap<>();
        converterMap.put(String.class, new StringConverter());
        converterMap.put(int.class, new IntConverter());
        converterMap.put(Integer.class, new IntConverter());
        converterMap.put(float.class, new FloatConverter());
        converterMap.put(Float.class, new FloatConverter());
        converterMap.put(double.class, new DoubleConverter());
        converterMap.put(Double.class, new DoubleConverter());
        converterMap.put(long.class, new LongConverter());
        converterMap.put(Long.class, new LongConverter());
        converterMap.put(boolean.class, new BooleanConverter());
        converterMap.put(Boolean.class, new BooleanConverter());
        converterMap.put(Date.class, new DateConverter());
    }

    public static String object2XmlConvert(Object o) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Class cls = o.getClass();
            if (cls.getName().startsWith("java.lang")) {
                return o.toString();
            }

            Field[] props = cls.getDeclaredFields();
            for (Field prop : props) {
                prop.setAccessible(true);
                String propName = prop.getName();
                Class propType = prop.getType();
                Object propValue = prop.get(o);
                StringBuilder stb = new StringBuilder();
                if (propValue == null) {
                    continue;
                }
                if (converterMap.containsKey(propType)) {
                    stb = new StringBuilder(converterMap.get(propType).xmlReverse(propValue, prop));
                } else if (propType.getName().equals("java.util.List")) {
                    List valueList = (List) propValue;
                    for (Object value : valueList) {
                        stb.append(appendXmlNode(propName, object2XmlConvert(value)));
                    }
                } else {
                    stb = new StringBuilder(appendXmlNode(propName, object2XmlConvert(propValue)));
                }
                stringBuilder.append(stb);
            }
        } catch (Exception e) {
            return "parser error : " + e.getMessage();
        }
        return stringBuilder.toString();
    }

    public static String appendXmlNode(String nodeName, String nodeValue) {
        StringBuilder sb = new StringBuilder();
        return sb.append("<").append(nodeName).append(">").append(nodeValue).append("</").append(nodeName).append(">").toString();
    }

    public static <T> T coverXml2Object(String xml, Class<T> clazz) {
        if (xml == null || "".equals(xml)) {
            return null;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xml)));
            Element root = doc.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            return handelNodes(nodeList, clazz);
        } catch (Exception e) {

        }
        return null;
    }

    public static <T> T handelNodes(NodeList nodeList, Class<T> clazz) {
        try {
            if (nodeList == null) {
                return null;
            }
            if (clazz.getName().startsWith("java.lang")) {
                return (T) nodeList.item(0).getNodeValue();
            }

            T obj = clazz.newInstance();
            Map<Field, List<Object>> listChildMap = new HashMap<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                String nodeName = node.getNodeName();
                short nodeType = node.getNodeType();
                if (nodeType == 3 || nodeType == 4) {
                    continue;
                }
                if (!node.hasChildNodes()) {
                    continue;
                }
                Field[] fields = clazz.getDeclaredFields();
                if (!isContainProp(fields, nodeName)) {
                    continue;
                }
                Field field = clazz.getDeclaredField(nodeName);
                field.setAccessible(true);
                Class<?> fieldType = field.getType();

                if (fieldType.getName().equals("java.util.List")) {
                    Type type = field.getGenericType();
                    if (type instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) type;
                        Class genericClazz = (Class) pt.getActualTypeArguments()[0];
                        Object listChildObj = handelNodes(node.getChildNodes(), genericClazz);

                        if (listChildMap.get(field) == null) {
                            List<Object> objectList = new ArrayList<>();
                            objectList.add(listChildObj);
                            listChildMap.put(field, objectList);
                        } else {
                            listChildMap.get(field).add(listChildObj);
                        }
                    }
                } else {
                    Converter converter = converterMap.get(fieldType);
                    if (converter == null) {
                        field.set(obj, handelNodes(node.getChildNodes(), fieldType));
                        continue;
                    }
                    Object convert = converter.convertXml(node, field);
                    if (convert != null) {
                        field.set(obj, convert);
                    }
                }
            }
            for (Field field : listChildMap.keySet()) {
                field.set(obj, listChildMap.get(field));
            }
            return obj;
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean isContainProp(Field[] fields, String nodeName) {
        if (fields == null || fields.length < 0) {
            return false;
        }
        if (nodeName == null || "".equals(nodeName)) {
            return false;
        }
        for (Field field : fields) {
            if (field.getName().equals(nodeName)) {
                return true;
            }
        }
        return false;
    }
}
