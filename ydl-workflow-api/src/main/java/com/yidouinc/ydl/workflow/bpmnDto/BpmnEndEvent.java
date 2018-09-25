package com.yidouinc.ydl.workflow.bpmnDto;

import java.util.List;

public class BpmnEndEvent {

	private String id;
	
	private String name;
	
	private List<BpmnEventListener> lisenerList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<BpmnEventListener> getLisenerList() {
		return lisenerList;
	}

	public void setLisenerList(List<BpmnEventListener> lisenerList) {
		this.lisenerList = lisenerList;
	}
 
}
