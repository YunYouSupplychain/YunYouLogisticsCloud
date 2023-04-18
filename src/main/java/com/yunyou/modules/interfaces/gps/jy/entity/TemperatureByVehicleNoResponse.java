package com.yunyou.modules.interfaces.gps.jy.entity;

import java.util.Date;

public class TemperatureByVehicleNoResponse {

    private String vehicleNo;// 车牌号
    private Date gpsTime;// GPS时间
    private Double lon;// 经度
    private Double lat;// 维度
    private Double speed;// 速度
    private Integer stat;// 状态 0 没超温 1 超温
    private String location;// 位置
    private Double tmp1;// 温度1
    private Double tmp2;// 温度2
    private Double tmp3;// 温度3
    private Double tmp4;// 温度4

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public Date getGpsTime() {
        return gpsTime;
    }

    public void setGpsTime(Date gpsTime) {
        this.gpsTime = gpsTime;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getStat() {
        return stat;
    }

    public void setStat(Integer stat) {
        this.stat = stat;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getTmp1() {
        return tmp1;
    }

    public void setTmp1(Double tmp1) {
        this.tmp1 = tmp1;
    }

    public Double getTmp2() {
        return tmp2;
    }

    public void setTmp2(Double tmp2) {
        this.tmp2 = tmp2;
    }

    public Double getTmp3() {
        return tmp3;
    }

    public void setTmp3(Double tmp3) {
        this.tmp3 = tmp3;
    }

    public Double getTmp4() {
        return tmp4;
    }

    public void setTmp4(Double tmp4) {
        this.tmp4 = tmp4;
    }
}
