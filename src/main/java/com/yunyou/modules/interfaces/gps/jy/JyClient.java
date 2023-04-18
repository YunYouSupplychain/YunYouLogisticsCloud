package com.yunyou.modules.interfaces.gps.jy;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.interfaces.gps.HttpPoolUtil;
import com.yunyou.modules.interfaces.gps.MessageDigestUtil;
import com.yunyou.modules.interfaces.gps.Request;
import com.yunyou.modules.interfaces.gps.Response;
import com.yunyou.modules.interfaces.gps.constant.HttpSchema;
import com.yunyou.modules.interfaces.gps.constant.Method;
import com.yunyou.modules.interfaces.gps.jy.constant.Constants;
import org.apache.http.Header;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JyClient {

    public static Response get(String path, Map<String, String> queries) throws Exception {
        Request request = new Request();
        request.setHost(HttpSchema.HTTPS + Constants.BASE_URL);
        request.setAccessId(Constants.ACCESS_ID);
        request.setSecretKey(Constants.SECRET_KEY);
        request.setPath(path);
        request.setMethod(Method.GET);
        request.setQueries(queries);
        return execute(request);
    }

    public static Response post(String path, Map<String, Object> queries) throws Exception {
        Request request = new Request();
        request.setHost(HttpSchema.HTTPS + Constants.BASE_URL);
        request.setAccessId(Constants.ACCESS_ID);
        request.setSecretKey(Constants.SECRET_KEY);
        request.setPath(path);
        request.setMethod(Method.POST_JSON);
        request.setHeaders(Maps.newHashMap());
        request.setJsonStrBody(queries == null ? null : JSON.toJSONString(queries));
        return execute(request);
    }

    private static Response execute(Request request) throws Exception {
        switch (request.getMethod()) {
            case GET:
                return httpGet(request.getHost(),
                        request.getPath(),
                        request.getHeaders(),
                        request.getQueries());
            case POST_JSON:
                return httpPost(request.getHost(),
                        request.getPath(),
                        request.getHeaders(),
                        request.getQueries(),
                        request.getJsonStrBody());
            case PUT_JSON:
                return httpPut(request.getHost(),
                        request.getPath(),
                        request.getHeaders(),
                        request.getQueries(),
                        request.getJsonStrBody());
            case DELETE:
                return httpDelete(request.getHost(),
                        request.getPath(),
                        request.getHeaders(),
                        request.getQueries());
            default:
                throw new IllegalArgumentException(String.format("unsupported method:%s", request.getMethod()));
        }
    }

    private static Response convert(CloseableHttpResponse response) throws Exception {
        Response res = new Response();
        if (null != response) {
            res.setStatusCode(response.getStatusLine().getStatusCode());
            for (Header header : response.getAllHeaders()) {
                res.setHeader(header.getName(), MessageDigestUtil.iso88591ToUtf8(header.getValue()));
            }

            res.setContentType(res.getHeader("Content-Type"));
            try {
                res.setBody(EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
            } finally {
                EntityUtils.consume(response.getEntity());
                response.close();
            }
        } else {
            //服务器无回应
            res.setStatusCode(500);
            res.setErrorMessage("No Response");
        }
        return res;
    }

    /**
     * HTTP GET
     */
    public static Response httpGet(String host, String path,
                                   Map<String, String> headers,
                                   Map<String, String> queries) throws Exception {

        HttpGet get = new HttpGet(HttpPoolUtil.getUrl(host, path, queries));
        return convert(HttpPoolUtil.getHttpClient().execute(get));
    }

    /**
     * HTTP POST
     */
    public static Response httpPost(String host, String path,
                                    Map<String, String> headers,
                                    Map<String, String> queries,
                                    String body) throws Exception {

        HttpPost post = new HttpPost(HttpPoolUtil.getUrl(host, path, queries));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            post.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }
        if (StringUtils.isNotBlank(body)) {
            post.setEntity(new StringEntity(body, StandardCharsets.UTF_8));

        }
        return convert(HttpPoolUtil.getHttpClient().execute(post));
    }

    /**
     * HTTP PUT 字符串
     */
    public static Response httpPut(String host, String path,
                                   Map<String, String> headers,
                                   Map<String, String> queries,
                                   String body) throws Exception {

        HttpPut put = new HttpPut(HttpPoolUtil.getUrl(host, path, queries));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            put.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }
        if (StringUtils.isNotBlank(body)) {
            put.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
        }
        return convert(HttpPoolUtil.getHttpClient().execute(put));
    }

    /**
     * HTTP DELETE
     */
    public static Response httpDelete(String host, String path,
                                      Map<String, String> headers,
                                      Map<String, String> queries) throws Exception {

        HttpDelete delete = new HttpDelete(HttpPoolUtil.getUrl(host, path, queries));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            delete.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }
        return convert(HttpPoolUtil.getHttpClient().execute(delete));
    }


    /*public static void main(String[] args) {
        Map<String, String> params = new HashMap<>();
        params.put("userName", Constants.ACCESS_ID);
        params.put("password", MD5Util.encrypt32(Constants.SECRET_KEY).toUpperCase());
        params.put("vehicleNo", "苏A061FE");
        params.put("startTime", "2016-05-16 09:25:00");
        params.put("endTime", "2016-05-16 09:26:00");
        params.put("hasLocation", "1");
        try {
            Response response = JyClient.get(GET_TEMPERATURE_BY_VEHICLE_NO, params);
            System.out.println(JSON.toJSONString(response));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
