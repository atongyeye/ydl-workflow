package com.yidouinc.ydl.workflow.bpmnDto;

import java.util.List;

public class BpmnEventListener {

	private String event;
	
	private String className;
	
	private List<BpmnEventField> fieldList;

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<BpmnEventField> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<BpmnEventField> fieldList) {
		this.fieldList = fieldList;
	}

}
