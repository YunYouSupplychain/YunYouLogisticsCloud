package com.yunyou.modules.wms.inventory.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 库存移动单
 */
public class BanQinWmMvHeader extends DataEntity<BanQinWmMvHeader> {

	private static final long serialVersionUID = 1L;
	private String mvNo;		// 移动单号
	private String ownerCode;		// 货主编码
	private String status;		// 移动状态
	private String auditStatus;		// 审核状态
	private String reasonCode;		// 原因编码
	private String reason;		// 原因描述
	private String auditOp;		// 审核人
	private Date auditTime;		// 审核时间
	private String auditComment;		// 审核意见
	private String mvOp;		// 移动操作人
	private Date mvTime;		// 移动时间
	private Date ediSendTime;		// EDI发送时间
	private String isEdiSend;		// EDI是否已发送
	private String def1;		// 自定义1
	private String def2;		// 自定义2
	private String def3;		// 自定义3
	private String def4;		// 自定义4
	private String def5;		// 自定义5
	private String orgId;		// 分公司

	public BanQinWmMvHeader() {
		super();
		this.recVer = 0;
	}

	public BanQinWmMvHeader(String id){
		super(id);
	}

	@ExcelField(title="移动单号", align=2, sort=2)
    public String getMvNo() {
        return mvNo;
    }

    public void setMvNo(String mvNo) {
        this.mvNo = mvNo;
    }

    @ExcelField(title="货主编码", align=2, sort=3)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="移动状态", align=2, sort=4)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="审核状态", align=2, sort=5)
	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	
	@ExcelField(title="原因编码", align=2, sort=6)
	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	
	@ExcelField(title="原因描述", align=2, sort=7)
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@ExcelField(title="审核人", align=2, sort=8)
	public String getAuditOp() {
		return auditOp;
	}

	public void setAuditOp(String auditOp) {
		this.auditOp = auditOp;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="审核时间", align=2, sort=9)
	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	
	@ExcelField(title="审核意见", align=2, sort=10)
	public String getAuditComment() {
		return auditComment;
	}

	public void setAuditComment(String auditComment) {
		this.auditComment = auditComment;
	}
	
	@ExcelField(title="移动操作人", align=2, sort=11)
    public String getMvOp() {
        return mvOp;
    }

    public void setMvOp(String mvOp) {
        this.mvOp = mvOp;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title="移动时间", align=2, sort=12)
    public Date getMvTime() {
        return mvTime;
    }

    public void setMvTime(Date mvTime) {
        this.mvTime = mvTime;
    }

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="EDI发送时间", align=2, sort=14)
	public Date getEdiSendTime() {
		return ediSendTime;
	}

	public void setEdiSendTime(Date ediSendTime) {
		this.ediSendTime = ediSendTime;
	}
	
	@ExcelField(title="EDI是否已发送", align=2, sort=15)
	public String getIsEdiSend() {
		return isEdiSend;
	}

	public void setIsEdiSend(String isEdiSend) {
		this.isEdiSend = isEdiSend;
	}
	
	@ExcelField(title="自定义1", align=2, sort=16)
	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}
	
	@ExcelField(title="自定义2", align=2, sort=17)
	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}
	
	@ExcelField(title="自定义3", align=2, sort=18)
	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}
	
	@ExcelField(title="自定义4", align=2, sort=19)
	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}
	
	@ExcelField(title="自定义5", align=2, sort=20)
	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}

	@ExcelField(title="分公司", align=2, sort=29)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}