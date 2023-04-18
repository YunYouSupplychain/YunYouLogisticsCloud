package com.yunyou.modules.bms.basic.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 客户Entity
 *
 * @author Jianhua Liu
 * @version 2019-06-11
 */
public class BmsCustomer extends DataEntity<BmsCustomer> {

    private static final long serialVersionUID = 1L;
    // 客户代码
    private String ebcuCustomerNo;
    // 中文名称
    private String ebcuNameCn;
    // 简称
    private String ebcuShortName;
    // 英文名称
    private String ebcuNameEn;
    // 客户类型
    private String ebcuType;
    // 行业类型:1化工,2物流,3仓储
    private String ebcuIndustryType;
    // 主营业务
    private String ebcuMainBusiness;
    // 电话
    private String ebcuTel;
    // 传真
    private String ebcuFax;
    // 邮编
    private String ebcuZipCode;
    // 地址
    private String ebcuAddress;
    // 财务代码
    private String ebcuFinanceCode;
    // 平台税务登记号
    private String ebcuTaxRegistNo;
    // 工商登记号
    private String ebcuBusinessNo;
    // 税率
    private Double ebcuTaxRate;
    // 税率值
    private Double ebcuTaxRateValue;
    // 是否一般纳税人
    private String ebcuIsGeneralTaxpayer;
    // 时区
    private String timeZone;
    // 对账人
    private String checkPerson;
    // 区域ID
    private String areaId;
    // 区域编码
    private String areaCode;
    // 区域名称
    private String areaName;
    // 项目
    private String project;
    // 机构ID
    private String orgId;

    public BmsCustomer() {
        super();
    }

    public BmsCustomer(String id) {
        super(id);
    }

    public String getEbcuCustomerNo() {
        return ebcuCustomerNo;
    }

    public void setEbcuCustomerNo(String ebcuCustomerNo) {
        this.ebcuCustomerNo = ebcuCustomerNo;
    }

    public String getEbcuNameCn() {
        return ebcuNameCn;
    }

    public void setEbcuNameCn(String ebcuNameCn) {
        this.ebcuNameCn = ebcuNameCn;
    }

    public String getEbcuShortName() {
        return ebcuShortName;
    }

    public void setEbcuShortName(String ebcuShortName) {
        this.ebcuShortName = ebcuShortName;
    }

    public String getEbcuNameEn() {
        return ebcuNameEn;
    }

    public void setEbcuNameEn(String ebcuNameEn) {
        this.ebcuNameEn = ebcuNameEn;
    }

    public String getEbcuType() {
        return ebcuType;
    }

    public void setEbcuType(String ebcuType) {
        this.ebcuType = ebcuType;
    }

    public String getEbcuIndustryType() {
        return ebcuIndustryType;
    }

    public void setEbcuIndustryType(String ebcuIndustryType) {
        this.ebcuIndustryType = ebcuIndustryType;
    }

    public String getEbcuMainBusiness() {
        return ebcuMainBusiness;
    }

    public void setEbcuMainBusiness(String ebcuMainBusiness) {
        this.ebcuMainBusiness = ebcuMainBusiness;
    }

    public String getEbcuTel() {
        return ebcuTel;
    }

    public void setEbcuTel(String ebcuTel) {
        this.ebcuTel = ebcuTel;
    }

    public String getEbcuFax() {
        return ebcuFax;
    }

    public void setEbcuFax(String ebcuFax) {
        this.ebcuFax = ebcuFax;
    }

    public String getEbcuZipCode() {
        return ebcuZipCode;
    }

    public void setEbcuZipCode(String ebcuZipCode) {
        this.ebcuZipCode = ebcuZipCode;
    }

    public String getEbcuAddress() {
        return ebcuAddress;
    }

    public void setEbcuAddress(String ebcuAddress) {
        this.ebcuAddress = ebcuAddress;
    }

    public String getEbcuFinanceCode() {
        return ebcuFinanceCode;
    }

    public void setEbcuFinanceCode(String ebcuFinanceCode) {
        this.ebcuFinanceCode = ebcuFinanceCode;
    }

    public String getEbcuTaxRegistNo() {
        return ebcuTaxRegistNo;
    }

    public void setEbcuTaxRegistNo(String ebcuTaxRegistNo) {
        this.ebcuTaxRegistNo = ebcuTaxRegistNo;
    }

    public String getEbcuBusinessNo() {
        return ebcuBusinessNo;
    }

    public void setEbcuBusinessNo(String ebcuBusinessNo) {
        this.ebcuBusinessNo = ebcuBusinessNo;
    }

    public Double getEbcuTaxRate() {
        return ebcuTaxRate;
    }

    public void setEbcuTaxRate(Double ebcuTaxRate) {
        this.ebcuTaxRate = ebcuTaxRate;
    }

    public Double getEbcuTaxRateValue() {
        return ebcuTaxRateValue;
    }

    public void setEbcuTaxRateValue(Double ebcuTaxRateValue) {
        this.ebcuTaxRateValue = ebcuTaxRateValue;
    }

    public String getEbcuIsGeneralTaxpayer() {
        return ebcuIsGeneralTaxpayer;
    }

    public void setEbcuIsGeneralTaxpayer(String ebcuIsGeneralTaxpayer) {
        this.ebcuIsGeneralTaxpayer = ebcuIsGeneralTaxpayer;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getCheckPerson() {
        return checkPerson;
    }

    public void setCheckPerson(String checkPerson) {
        this.checkPerson = checkPerson;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}