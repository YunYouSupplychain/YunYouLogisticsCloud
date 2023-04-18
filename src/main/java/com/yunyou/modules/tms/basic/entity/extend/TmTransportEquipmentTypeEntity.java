package com.yunyou.modules.tms.basic.entity.extend;

import com.yunyou.modules.tms.basic.entity.TmTransportEquipmentType;

public class TmTransportEquipmentTypeEntity extends TmTransportEquipmentType {
    private static final long serialVersionUID = -1439261380151518470L;

    /*多字段匹配查询*/
    private String codeAndName;

    public String getCodeAndName() {
        return codeAndName;
    }

    public void setCodeAndName(String codeAndName) {
        this.codeAndName = codeAndName;
    }
}
