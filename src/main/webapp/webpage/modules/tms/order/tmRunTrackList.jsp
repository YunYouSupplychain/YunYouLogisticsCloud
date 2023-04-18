<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>行车轨迹</title>
    <script src="${ctxStatic}/plugin/jquery/3.5.1/jquery-3.5.1.min.js"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=3.0&ak=LhXrawUfyMia5MGWkmk531Ksx4Ypq7aX"></script>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <link href="${ctxStatic}/common/css/bqStyle.css?v=${fns:getConfig('css.version')}" rel="stylesheet"/>
    <style type="text/css">
        body, html, #b-map {
            width: 100%;
            height: 100%;
            overflow: hidden;
            padding-left: 5px;
            padding-top: 5px;
            margin: 0;;
        }
    </style>
</head>

<body>
<div style="width: 100%; padding-left: 5px; padding-top: 5px; padding-bottom: 5px;display: none;">
    <button id="run" class="btn btn-primary btn-sm" onclick="run()">轨迹回放</button>
    <button id="stop" class="btn btn-primary btn-sm" onclick="stop()">停止</button>
    <button id="pause" class="btn btn-primary btn-sm" onclick="pause()">暂停</button>
</div>
<div id="b-map"></div>
<script>
    // 实例化地图
    var map = new BMap.Map("b-map");
    // 设置地图中心点以及最大缩放级别
    map.centerAndZoom(new BMap.Point(116.404, 39.915), 16);
    // 开启鼠标滚轮缩放
    map.enableScrollWheelZoom(true);

    /**
     * 绘制路线
     * @param data 车辆行驶轨迹<车号,[行驶轨迹坐标点]>
     */
    function drawPolyline(data) {
        // 绘制箭头及其样式
        var symbol = new BMap.Symbol(BMap_Symbol_SHAPE_BACKWARD_OPEN_ARROW, {
            scale: 0.6,// 图标缩放大小
            strokeColor: '#fff',// 设置矢量图标的线填充颜色
            strokeWeight: 2,// 设置线宽
        });
        // 线路样式
        var styleOptions = {
            strokeColor: '#01bf34',
            strokeWeight: 8,
            strokeOpacity: 0.8,
            strokeStyle: 'solid',
            icons: [new BMap.IconSequence(symbol, '100%', '3%', false)]
        };
        for (var vehicleNo in data) {
            var points = [];
            var tracks = data[vehicleNo];
            for (var i = 0; i < tracks.length; i++) {
                points.push(new BMap.Point(tracks[i].lng, tracks[i].lat));
            }
            map.addOverlay(new BMap.Polyline(points, styleOptions));
            map.centerAndZoom(points[points.length / 2], 11);
        }
    }

    /*// 百度地图API功能
    var map = new BMap.Map("container");
    map.centerAndZoom(new BMap.Point(108.552500, 34.322700), 6);
    map.enableScrollWheelZoom();
    var lushu; // 路书
    var s1, t1, s2, t2;// 起始点经度、起始点纬度、目的地经度、目的地纬度
    var startL = '${tmTransportOrderEntity.shipAddress}';
    var endL = '${tmTransportOrderEntity.consigneeAddress}';

    // 获取数据
    // 获取驾车获取数据
    var drv = new BMap.DrivingRoute(map, {
        onSearchComplete: function (res) {
            if (drv.getStatus() === BMAP_STATUS_SUCCESS) {
                var plan = res.getPlan(0);
                var arrPois = [];// 地图点数据
                for (var j = 0; j < plan.getNumRoutes(); j++) {
                    var route = plan.getRoute(j);
                    arrPois = arrPois.concat(route.getPath());
                }
                // 添加轨迹
                addRoute(arrPois);
            }
        }
    });

    // 获取数据
    s1 = s1 ? parseFloat(s1) : 116.404844;
    t1 = t1 ? parseFloat(t1) : 39.911836;
    s2 = s2 ? parseFloat(s2) : 116.308102;
    t2 = t2 ? parseFloat(t2) : 40.056057;
    var start = new BMap.Point(s1, t1);
    var end = new BMap.Point(s2, t2);
    drv.search(start, end);*/

    // 添加路线
    function addRoute(data) {
        // 起始点、终点标记
        addMarker(new BMap.Point(data[0].lng, data[0].lat), '', false, startL);
        addMarker(new BMap.Point(data[data.length - 1].lng, data[data.length - 1].lat), '', false, endL);
        // 画线
        map.addOverlay(new BMap.Polyline(data, {strokeColor: '#18a45b', strokeOpacity: 0.8, strokeWeight: '8'}));
        // 设置点全部在视野中
        map.setViewport(data);
        // 路书
        lushu = new BMapLib.LuShu(map, data, {
            defaultContent: '',
            autoView: true,// 是否开启自动视野调整，如果开启那么路书在运动过程中会根据视野自动调整
            icon: new BMap.Icon('${ctxStatic}/car.png', new BMap.Size(52, 26), {anchor: new BMap.Size(27, 13)}),
            speed: 10000,
            enableRotation: true,// 是否设置marker随着道路的走向进行旋转
            landmarkPois: []
        });
    }

    // 添加点
    function addMarker(point, gpsTime, b, location) {
        var marker = new BMap.Marker(point);
        map.addOverlay(marker);
        if (b)
            marker.hide();
        var label = new BMap.Label("地点：" + location, {offset: new BMap.Size(20, -10)});
        marker.setLabel(label);
        label.hide();

        marker.addEventListener("mouseover", onMouseover);// 鼠标进入事件
        marker.addEventListener("mouseout", onMouseout);// 鼠标移出事件
    }

    // 画线
    function addLine(points) {
        // 画线
        var sy = new BMap.Symbol(BMap_Symbol_SHAPE_BACKWARD_OPEN_ARROW, {
            scale: 0.6,// 图标缩放大小
            strokeColor: '#fff',// 设置矢量图标的线填充颜色
            strokeWeight: '2'// 设置线宽
        });
        var icons = new BMap.IconSequence(sy, '100%', '4%');
        var polyline = new BMap.Polyline(points, {
            icons: [icons],
            strokeWeight: '8',// 折线的宽度，以像素为单位
            strokeOpacity: 0.8,// 折线的透明度，取值范围0 - 1
            strokeColor: "#18a45b" // 折线颜色
        });
        map.addOverlay(polyline);   // 增加折线
    }

    // 鼠标进入事件
    function onMouseover(e) {
        e.target.getLabel().show();
    }

    // 鼠标移出事件
    function onMouseout(e) {
        e.target.getLabel().hide();
    }

    function run() {
        lushu.start();
    }

    function stop() {
        lushu.stop();
    }

    function pause() {
        lushu.pause();
    }

    function hide() {
        lushu.hideInfoWindow();
    }

    function show() {
        lushu.showInfoWindow();
    }
</script>
</body>
</html>