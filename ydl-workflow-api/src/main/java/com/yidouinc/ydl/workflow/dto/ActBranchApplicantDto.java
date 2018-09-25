package com.yidouinc.ydl.workflow.dto;

import java.util.Date;

import com.yidouinc.ydl.workflow.response.ProcBranchResponse;

public class ActBranchApplicantDto {
    private Long id;

    private Long actBranchId;

    private Long companyId;

    private String moduleType;

    private String procDefKey;

    private Long applyId;

    private String applyName;
    
    private Byte applyType;

    private Long creatorId;

    private Date createTime;

    private ProcBranchResponse branchUsers;// 审批人，抄送人信息
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActBranchId() {
        return actBranchId;
    }

    public void setActBranchId(Long actBranchId) {
        this.actBranchId = actBranchId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getProcDefKey() {
        return procDefKey;
    }

    public void setProcDefKey(String procDefKey) {
        this.procDefKey = procDefKey;
    }

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public Byte getApplyType() {
        return applyType;
    }

    public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	public void setApplyType(Byte applyType) {
        this.applyType = applyType;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public ProcBranchResponse getBranchUsers() {
		return branchUsers;
	}

	public void setBranchUsers(ProcBranchResponse branchUsers) {
		this.branchUsers = branchUsers;
	}
    
    
}