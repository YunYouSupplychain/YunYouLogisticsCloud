package com.yunyou.modules.interfaces.tmApp.entity;

import java.io.Serializable;

public class TmAppDeleteCustomerOrderRequest implements Serializable {

    private String planOrderNo;
    private String orgId;

    public String getPlanOrderNo() {
        return planOrderNo;
    }

    public void setPlanOrderNo(String planOrderNo) {
        this.planOrderNo = planOrderNo;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }


}