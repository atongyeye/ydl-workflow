package com.yidouinc.ydl.workflow.dto;

import java.io.Serializable;
import java.util.List;

public class FlowElementDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4805299130877180624L;

	private String id;
	
	private String name;
	
	private List<SequenceFlowDto> incoming;
	
	private List<SequenceFlowDto> outgoing;
	
	private List<TaskUserDto> taskUserList;
	
	private String elementType;//0:StartEvent,1:UserTask,2:EndEvent,3:ExclusiveGateway,4:ParallelGateway,5:SequenceFlow,
	
	private String processInstanceId;
	
	private String businessKey;

	private String procDefId;

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

	public List<SequenceFlowDto> getIncoming() {
		return incoming;
	}

	public void setIncoming(List<SequenceFlowDto> incoming) {
		this.incoming = incoming;
	}

	public List<SequenceFlowDto> getOutgoing() {
		return outgoing;
	}

	public void setOutgoing(List<SequenceFlowDto> outgoing) {
		this.outgoing = outgoing;
	}

	public List<TaskUserDto> getTaskUserList() {
		return taskUserList;
	}

	public void setTaskUserList(List<TaskUserDto> taskUserList) {
		this.taskUserList = taskUserList;
	}

	public String getElementType() {
		return elementType;
	}

	public void setElementType(String elementType) {
		this.elementType = elementType;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getProcDefId() {
		return procDefId;
	}

	public void setProcDefId(String procDefId) {
		this.procDefId = procDefId;
	}

	

	
}