<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <title>车辆位置</title>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=3.0&ak=Gh5ZIDVX3XyNbwOEkGbGLGCHNnzxY27i"></script>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <style type="text/css">
        body, html{width: 100%;height: 100%;margin:0;}
        #allmap{height:100%;width:100%;}
        #r-result{width:100%; font-size:14px;}
    </style>
</head>

<body>
    <div id="allmap"></div>
</body>
<script>
    var map = new BMap.Map("allmap");
    map.centerAndZoom(new BMap.Point(108.552500, 34.322700), 6);
    map.enableScrollWheelZoom(true);
    var myIcon = new BMap.Icon('${ctxStatic}/car.png', new BMap.Size(52, 26));
    var points = [
        {lc: "120.0834358100,30.0756548900", carNo: "沪C56QY1"},
        {lc: "119.5752537400,30.3703460600", carNo: "鲁CTA668"},
        {lc: "109.2105130800,34.6033627300", carNo: "鲁NQ9126"},
        {lc: "112.1109316500,35.8952740000", carNo: "沪A21123"},
        {lc: "113.9127397500,33.6572913800", carNo: "闽F66653"},
        {lc: "116.3792256600,31.3478740000", carNo: "闽A77866"}
    ];
    if (points.length > 0) {
        var pointArray = points;
        map.clearOverlays();
        for (var index in pointArray) {
            var value = pointArray[index]['lc'];
            var x = getV(value, 0);
            var y = getV(value, 1);
            var carNo = pointArray[index]['carNo'];
            var new_point = new BMap.Point(x, y);
            // 创建标注
            var marker = new BMap.Marker(new_point, {icon: myIcon});
            var label = new BMap.Label(carNo, {offset: new BMap.Size(20, -10)});
            label.setStyle(getLabelStyle());
            marker.setLabel(label);
            // 将标注添加到地图中
            map.addOverlay(marker);
        }
    }
    
    function getV(value, index) {
        return parseFloat(value.split(',')[index]);
    }

    function getLabelStyle() {
        return {
            color: "#fff",
            backgroundColor: "#333333",
            border: "0",
            width: '60px',
            opacity:"0.6",
            verticalAlign:"center",
            borderRadius: "2px",
            whiteSpace:"normal",
            wordWrap:"break-word",
            padding:"2px"
        };
    }
</script>
</html>