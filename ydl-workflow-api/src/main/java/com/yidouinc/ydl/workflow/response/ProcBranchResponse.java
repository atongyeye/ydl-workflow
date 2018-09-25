package com.yidouinc.ydl.workflow.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProcBranchResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7553062761208839138L;

	private String procDefkey;

	private Long companyId;

	private List<BranchPersonDto> auditorList = new ArrayList<BranchPersonDto>();

	private List<BranchPersonDto> ccPersonList = new ArrayList<BranchPersonDto>();

	public String getProcDefkey() {
		return procDefkey;
	}

	public void setProcDefkey(String procDefkey) {
		this.procDefkey = procDefkey;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public List<BranchPersonDto> getAuditorList() {
		return auditorList;
	}

	public void setAuditorList(List<BranchPersonDto> auditorList) {
		this.auditorList = auditorList;
	}

	public List<BranchPersonDto> getCcPersonList() {
		return ccPersonList;
	}

	public void setCcPersonList(List<BranchPersonDto> ccPersonList) {
		this.ccPersonList = ccPersonList;
	}

}
