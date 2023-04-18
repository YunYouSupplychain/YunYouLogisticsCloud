package com.yunyou.modules.tms.order.entity.extend;

import com.yunyou.modules.tms.order.entity.TmTransportOrderLabel;

public class TmTransportOrderLabelEntity extends TmTransportOrderLabel {
    private static final long serialVersionUID = 817138864098624125L;
    private String skuName;

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }
}
