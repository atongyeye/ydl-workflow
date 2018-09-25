package com.yidouinc.ydl.workflow.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class ProcessDetailDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8379166329793936311L;

	private Long companyId;

	private String procInstId;// 流程实例id

	private String code;// 审批单号

	private String procDefKey;// 流程key

	private String businessKey;// 模块主键

	private String businessType;// 模块类型

	private String businessName;// 模块名称

	private Long businessManagerId;//模块负责人
	
	private Long startUserId;// 流程发起人id

	private String startUserName;// 流程发起人名称

	private String status;// 0:xx审批中，1:审批同过,2:审批驳回,3:已撤销

	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date startTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date endTime;

	private List<ActOperatingDto> operatingList = new ArrayList<ActOperatingDto>();// 操作列表

	private Long operatingFormId;// 审批单id

	private ActBusinessFormDto businessFormDto;// 业务表单

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getProcInstId() {
		return procInstId;
	}

	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public Long getBusinessManagerId() {
		return businessManagerId;
	}

	public void setBusinessManagerId(Long businessManagerId) {
		this.businessManagerId = businessManagerId;
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

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public List<ActOperatingDto> getOperatingList() {
		return operatingList;
	}

	public void setOperatingList(List<ActOperatingDto> operatingList) {
		this.operatingList = operatingList;
	}

	public Long getOperatingFormId() {
		return operatingFormId;
	}

	public void setOperatingFormId(Long operatingFormId) {
		this.operatingFormId = operatingFormId;
	}

	public ActBusinessFormDto getBusinessFormDto() {
		return businessFormDto;
	}

	public void setBusinessFormDto(ActBusinessFormDto businessFormDto) {
		this.businessFormDto = businessFormDto;
	}

	

	
}
