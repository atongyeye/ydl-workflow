package com.yidouinc.ydl.workflow.bpmnDto;

public class BpmnSequenceFlow {

	private  String id;
	
	private  String name;
	
	private  String sourceRef;
	
	private  String targetRef;
	
	/**
	 * 判断条件
	 */
	private  String conditionExpression;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSourceRef() {
		return sourceRef;
	}

	public void setSourceRef(String sourceRef) {
		this.sourceRef = sourceRef;
	}

	public String getTargetRef() {
		return targetRef;
	}

	public void setTargetRef(String targetRef) {
		this.targetRef = targetRef;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConditionExpression() {
		return conditionExpression;
	}

	public void setConditionExpression(String conditionExpression) {
		this.conditionExpression = conditionExpression;
	}
	
}
