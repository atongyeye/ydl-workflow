package com.yidouinc.ydl.workflow.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.yidouinc.ydl.workflow.bpmnDto.BpmnModleDto;
import com.yidouinc.ydl.workflow.response.ProcBranchResponse;

public class ActAuditBranchDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7047276225666294567L;

	private Long id;

	private String name;

	private Long actConfigId;

	private Long companyId;

	private String moduleType;

	private String procDefKey;

	private Byte preCondition;

	private Byte status;

	private Long creatorId;

	private Long updatorId;

	private Date createTime;

	private Date updateTime;

	private BpmnModleDto bpmnModleDto;// 生成分支流程定义

	private List<Long> ccPersonIds = new ArrayList<Long>();// 分支抄送人id列表

	private ProcBranchResponse branchUsers;// 审批人，抄送人信息

	private List<Long> userIds = new ArrayList<Long>();// 申请人列表-用户

	private List<Long> deptIds = new ArrayList<Long>();// 申请人列表-部门

	private List<ActBranchApplicantDto> applicantList = new ArrayList<ActBranchApplicantDto>();// 申请人信息

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getActConfigId() {
		return actConfigId;
	}

	public void setActConfigId(Long actConfigId) {
		this.actConfigId = actConfigId;
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

	public Byte getPreCondition() {
		return preCondition;
	}

	public void setPreCondition(Byte preCondition) {
		this.preCondition = preCondition;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public Long getUpdatorId() {
		return updatorId;
	}

	public void setUpdatorId(Long updatorId) {
		this.updatorId = updatorId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public BpmnModleDto getBpmnModleDto() {
		return bpmnModleDto;
	}

	public void setBpmnModleDto(BpmnModleDto bpmnModleDto) {
		this.bpmnModleDto = bpmnModleDto;
	}

	public List<Long> getCcPersonIds() {
		return ccPersonIds;
	}

	public void setCcPersonIds(List<Long> ccPersonIds) {
		this.ccPersonIds = ccPersonIds;
	}

	public ProcBranchResponse getBranchUsers() {
		return branchUsers;
	}

	public void setBranchUsers(ProcBranchResponse branchUsers) {
		this.branchUsers = branchUsers;
	}

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	public List<Long> getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(List<Long> deptIds) {
		this.deptIds = deptIds;
	}

	public List<ActBranchApplicantDto> getApplicantList() {
		return applicantList;
	}

	public void setApplicantList(List<ActBranchApplicantDto> applicantList) {
		this.applicantList = applicantList;
	}

}