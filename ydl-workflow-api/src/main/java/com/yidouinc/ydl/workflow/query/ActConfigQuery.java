package com.yidouinc.ydl.workflow.query;

import java.io.Serializable;

public class ActConfigQuery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7860439052540298221L;

	private long companyId;

	private String moduleType;

	private Long auditConfigId;

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public Long getAuditConfigId() {
		return auditConfigId;
	}

	public void setAuditConfigId(Long auditConfigId) {
		this.auditConfigId = auditConfigId;
	}

}
