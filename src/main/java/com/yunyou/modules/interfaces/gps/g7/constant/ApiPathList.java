package com.yunyou.modules.interfaces.gps.g7.constant;

import com.yunyou.modules.interfaces.gps.constant.Method;

/**
 * G7接口列表
 * 前缀为Http请求方法{@link Method}
 */
public class ApiPathList {

    /**
     * 根据GPS设备号获取当前车辆位置
     */
    public static final String POST_VEHICLE_LOCATION_BY_GPS_NO = "/v1/device/equipment/simple_currents_by_gpsnos";

    /**
     * 根据GPS设备号获取车辆轨迹
     */
    public static final String GET_VEHICLE_TRACKS_BY_GPS_NO = "/v1/device/truck/history_location_by_gpsno";

    /**
     * 根据GPS设备号获取温度明细
     */
    public static final String GET_GPS_EQUIPMENT_TEMPERATURE = "/v1/coldchain/daily/temperature";

    /**
     * 批量查询车辆状态
     */
    public static final String GET_BATCH_TRUCK_CUR_INFO = "/v1/device/truck/current_info/batch";

    /**
     * 匹配电子围栏
     */
    public static final String POST_MATCH_MAP_FENCE = "/v1/map/mapfence/match";
}
