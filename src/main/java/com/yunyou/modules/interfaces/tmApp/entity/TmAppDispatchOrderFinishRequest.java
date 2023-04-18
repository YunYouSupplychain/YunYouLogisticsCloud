package com.yunyou.modules.interfaces.tmApp.entity;

import java.io.Serializable;

public class TmAppDispatchOrderFinishRequest implements Serializable {

    private String dispatchId;
    private String carNo;
    private Double odometerNumber;

    private String orgId;
    private String baseOrgId;
    private String operator;

    public String getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(String dispatchId) {
        this.dispatchId = dispatchId;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public Double getOdometerNumber() {
        return odometerNumber;
    }

    public void setOdometerNumber(Double odometerNumber) {
        this.odometerNumber = odometerNumber;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }
}