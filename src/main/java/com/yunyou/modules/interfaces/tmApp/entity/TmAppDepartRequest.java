package com.yunyou.modules.interfaces.tmApp.entity;

import java.io.Serializable;

public class TmAppDepartRequest implements Serializable {

    private String dispatchId;

    public String getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(String dispatchId) {
        this.dispatchId = dispatchId;
    }
}