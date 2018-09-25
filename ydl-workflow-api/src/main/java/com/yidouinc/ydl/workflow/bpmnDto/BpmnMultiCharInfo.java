package com.yidouinc.ydl.workflow.bpmnDto;

public class BpmnMultiCharInfo {

	private String userTaskId;
	
	
	private String elementVariable;
	
	/**
	 * 审批人 多人
	 */
	private String inputDataItem;
	
	/**
	 * 条件
	 */
	private String condition ;
	
	
	private String sequential;
	
	public String getSequential() {
		return sequential;
	}

	public void setSequential(String sequential) {
		this.sequential = sequential;
	}

	public String getUserTaskId() {
		return userTaskId;
	}

	public void setUserTaskId(String userTaskId) {
		this.userTaskId = userTaskId;
	}

	public String getElementVariable() {
		return elementVariable;
	}

	public void setElementVariable(String elementVariable) {
		this.elementVariable = elementVariable;
	}

	public String getInputDataItem() {
		return inputDataItem;
	}

	public void setInputDataItem(String inputDataItem) {
		this.inputDataItem = inputDataItem;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
	
}
