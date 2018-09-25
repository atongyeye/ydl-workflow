package com.yidouinc.ydl.workflow.dto;

import java.io.Serializable;
import java.util.Date;

public class ActOperatingDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1725019291734222023L;

	private Long id;

	private Long operatingFormId;

	private Long companyId;

	private Long operatorId;

	private String operatorName;

	private Date operatingTime;

	private String procInstId;

	private Byte type;

	private String content;

	private Date createTime;
	
	private String avatar;
	
	private String typeDesc;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOperatingFormId() {
		return operatingFormId;
	}

	public void setOperatingFormId(Long operatingFormId) {
		this.operatingFormId = operatingFormId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Date getOperatingTime() {
		return operatingTime;
	}

	public void setOperatingTime(Date operatingTime) {
		this.operatingTime = operatingTime;
	}

	public String getProcInstId() {
		return procInstId;
	}

	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	
	
	
}