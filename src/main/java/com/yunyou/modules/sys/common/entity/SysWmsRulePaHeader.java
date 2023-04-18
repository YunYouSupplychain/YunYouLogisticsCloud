package com.yunyou.modules.sys.common.entity;

import com.google.common.collect.Lists;
import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 质检规则Entity
 */
public class SysWmsRulePaHeader extends DataEntity<SysWmsRulePaHeader> {

    private static final long serialVersionUID = 1L;
    @NotNull(message = "规则编码不能为空")
    private String ruleCode;// 规则编码
    @NotNull(message = "规则名称不能为空")
    private String ruleName;// 规则名称
    @NotNull(message = "数据套不能为空")
    private String dataSet;// 数据套

    private List<SysWmsRulePaDetail> rulePaDetailList = Lists.newArrayList();
    private String dataSetName;// 数据套名称

    public SysWmsRulePaHeader() {
        super();
    }

    public SysWmsRulePaHeader(String id) {
        super(id);
    }

    public SysWmsRulePaHeader(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }

    public List<SysWmsRulePaDetail> getRulePaDetailList() {
        return rulePaDetailList;
    }

    public void setRulePaDetailList(List<SysWmsRulePaDetail> rulePaDetailList) {
        this.rulePaDetailList = rulePaDetailList;
    }
}