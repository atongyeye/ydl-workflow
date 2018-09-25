package com.yidouinc.ydl.workflow.query;

import java.io.Serializable;

public class WorkflowQuery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7860439052540298221L;

	private Long companyId;

	private Long userId;// 审核人,发起人或抄送人

	private int pageSize = 10;

	private int currentPage = 1;

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

}
