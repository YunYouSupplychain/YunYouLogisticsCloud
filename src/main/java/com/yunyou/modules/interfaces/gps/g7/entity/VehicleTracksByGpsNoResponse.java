package com.yunyou.modules.interfaces.gps.g7.entity;

import java.io.Serializable;

public class VehicleTracksByGpsNoResponse implements Serializable {

    private static final long serialVersionUID = 4177747537452835555L;
    // 经度，GCJ02坐标系
    private Double lng;
    private Double lat;
    // 速度
    private Integer speed;
    // 方向角度
    private Integer course;
    // 时间
    private Long time;
    // 距上一点距离
    private Long distance;

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getCourse() {
        return course;
    }

    public void setCourse(Integer course) {
        this.course = course;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }
}
