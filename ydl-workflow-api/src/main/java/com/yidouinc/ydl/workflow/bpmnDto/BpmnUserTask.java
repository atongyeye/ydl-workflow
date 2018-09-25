package com.yidouinc.ydl.workflow.bpmnDto;

import java.util.List;

public class BpmnUserTask {

	private String id;
	
	private String name;
	
	private String[] assignee;
	
	private String documentation;
	
	private BpmnMultiCharInfo charInfo;
	
	private List<BpmnEventListener> lisenerList;

	
	public BpmnMultiCharInfo getCharInfo() {
		return charInfo;
	}

	public void setCharInfo(BpmnMultiCharInfo charInfo) {
		this.charInfo = charInfo;
	}

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

	public String[] getAssignee() {
		return assignee;
	}

	public void setAssignee(String[] assignee) {
		this.assignee = assignee;
	}

	public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	public List<BpmnEventListener> getLisenerList() {
		return lisenerList;
	}

	public void setLisenerList(List<BpmnEventListener> lisenerList) {
		this.lisenerList = lisenerList;
	}

}
