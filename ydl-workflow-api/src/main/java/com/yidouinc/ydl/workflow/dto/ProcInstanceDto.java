package com.yidouinc.ydl.workflow.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class ProcInstanceDto implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8379166329793936311L;

	private Long companyId;
	
	private String procInstId;//流程实例id

	private String procDefKey;//流程key
	
	private String businessKey;//模块主键
	
	private String businessType;//模块类型

	private String businessName;//模块名称
	
	private Long startUserId;//流程发起人id
	
	private String startUserName;//流程发起人名称
	
	private String assignee;//当前审批人id
	
	private String assignName;//当前审批人名称
	
	private String status;// 0:xx审批中，1:审批同过,2:审批驳回,3:已撤销
	
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    private Date startTime;//流程申请时间
	
	private Long operatorId;//操作人
	
	private byte operatingType;//1:待处理,2:发起,3:通过,4:驳回,5:重新发起,6:评论,7:撤销
	
	private String content;//操作批注
	
	private Long operatingFormId;//审批单id
	

	public String getProcInstId() {
		return procInstId;
	}

	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}

	public String getProcDefKey() {
		return procDefKey;
	}

	public void setProcDefKey(String procDefKey) {
		this.procDefKey = procDefKey;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getStartUserId() {
		return startUserId;
	}

	public void setStartUserId(Long startUserId) {
		this.startUserId = startUserId;
	}

	public String getStartUserName() {
		return startUserName;
	}

	public void setStartUserName(String startUserName) {
		this.startUserName = startUserName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public byte getOperatingType() {
		return operatingType;
	}

	public void setOperatingType(byte operatingType) {
		this.operatingType = operatingType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getOperatingFormId() {
		return operatingFormId;
	}

	public void setOperatingFormId(Long operatingFormId) {
		this.operatingFormId = operatingFormId;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getAssignName() {
		return assignName;
	}

	public void setAssignName(String assignName) {
		this.assignName = assignName;
	}
	
	
	
}
