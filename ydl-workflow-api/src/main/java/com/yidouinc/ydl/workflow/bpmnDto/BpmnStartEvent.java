package com.yidouinc.ydl.workflow.bpmnDto;

import java.util.List;

public class BpmnStartEvent {

	private String id;
	
	private String name;
	
	private String initiator;
	
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

	public String getInitiator() {
		return initiator;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	public List<BpmnEventListener> getLisenerList() {
		return lisenerList;
	}

	public void setLisenerList(List<BpmnEventListener> lisenerList) {
		this.lisenerList = lisenerList;
	}

	
}
