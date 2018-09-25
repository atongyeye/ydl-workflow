package com.yidouinc.ydl.workflow.bpmnDto;

import java.util.List;

public class BpmnGraphInfo {
	
	private String id;
	
	/**
	 * 1:节点  2:连建线
	 */
	private String type;
	
	private List<BpmnPublicGraphInfo> list;
	
	public List<BpmnPublicGraphInfo> getList() {
		return list;
	}

	public void setList(List<BpmnPublicGraphInfo> list) {
		this.list = list;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
