package com.yidouinc.ydl.workflow.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

public class TaskUserDto implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8926880649265853088L;

	private String taskId;
	
	private String taskKey;
	
	private String taskName;

	private String businessKey;

	private String businessTitle;
	
	private String assignee;
	
	Map<String, Object> variables;
	
	private String comment;
	
	private String companyId;
	
	private String formKey;
	
	private String activitiId;
	
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    private Date startTime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    private Date endTime;

	private String taskStatus;//0:已执行,1:待执行,2:未到达,3:驳回
	
	private String processInstanceId;
	
	private String assigneeName;//审批人
	
	private String ddId;
	
	private List<String> assigneeUsers;
	
	
	/*----会签变量开始--开始----*/
	private String inputDataItem;//审批人信息
	
	private String loopCardinality;
	
	private String completionCondition;//条件表达式
	
	private String elementVariable;//当前
	
	private boolean sequential;
	/*----会签变量--结束----*/
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskKey() {
		return taskKey;
	}

	public void setTaskKey(String taskKey) {
		this.taskKey = taskKey;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getBusinessTitle() {
		return businessTitle;
	}

	public void setBusinessTitle(String businessTitle) {
		this.businessTitle = businessTitle;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public Map<String, Object> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getFormKey() {
		return formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	public String getActivitiId() {
		return activitiId;
	}

	public void setActivitiId(String activitiId) {
		this.activitiId = activitiId;
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

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}

	public String getDdId() {
		return ddId;
	}

	public void setDdId(String ddId) {
		this.ddId = ddId;
	}

	public List<String> getAssigneeUsers() {
		return assigneeUsers;
	}

	public void setAssigneeUsers(List<String> assigneeUsers) {
		this.assigneeUsers = assigneeUsers;
	}

	public String getInputDataItem() {
		return inputDataItem;
	}

	public void setInputDataItem(String inputDataItem) {
		this.inputDataItem = inputDataItem;
	}

	public String getLoopCardinality() {
		return loopCardinality;
	}

	public void setLoopCardinality(String loopCardinality) {
		this.loopCardinality = loopCardinality;
	}

	public String getCompletionCondition() {
		return completionCondition;
	}

	public void setCompletionCondition(String completionCondition) {
		this.completionCondition = completionCondition;
	}

	public String getElementVariable() {
		return elementVariable;
	}

	public void setElementVariable(String elementVariable) {
		this.elementVariable = elementVariable;
	}


	public boolean isSequential() {
		return sequential;
	}

	public void setSequential(boolean sequential) {
		this.sequential = sequential;
	}
	

	
	

	
}