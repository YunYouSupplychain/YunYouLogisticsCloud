package com.yunyou.modules.interfaces.gps.jy.entity;

import java.util.Date;

public class TrackInfoByVehicleNoResponse {

    // 经度
    private Double longitude;
    // 纬度
    private Double latitude;
    // 地址
    private String location;
    // 时间
    private Date time;
    // 温度1
    private Double temperature1;
    // 温度2
    private Double temperature2;
    // 温度3
    private Double temperature3;
    // 温度4
    private Double temperature4;
    // 速度（公里/小时）
    private Integer speed;
    // 里程（公里）
    private Double mile;
    // 油量（升）
    private Double oil;
    // 停车时间（分钟）
    private Double lastTime;
    // 方向
    private Integer direction;

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Double getTemperature1() {
        return temperature1;
    }

    public void setTemperature1(Double temperature1) {
        this.temperature1 = temperature1;
    }

    public Double getTemperature2() {
        return temperature2;
    }

    public void setTemperature2(Double temperature2) {
        this.temperature2 = temperature2;
    }

    public Double getTemperature3() {
        return temperature3;
    }

    public void setTemperature3(Double temperature3) {
        this.temperature3 = temperature3;
    }

    public Double getTemperature4() {
        return temperature4;
    }

    public void setTemperature4(Double temperature4) {
        this.temperature4 = temperature4;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Double getMile() {
        return mile;
    }

    public void setMile(Double mile) {
        this.mile = mile;
    }

    public Double getOil() {
        return oil;
    }

    public void setOil(Double oil) {
        this.oil = oil;
    }

    public Double getLastTime() {
        return lastTime;
    }

    public void setLastTime(Double lastTime) {
        this.lastTime = lastTime;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }
}
