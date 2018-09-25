package com.yidouinc.ydl.workflow.bpmnDto;

import java.util.List;

public class BpmnModleDto {

	/**
	 * 流程节点
	 */
	private String processId;
	
	/**
	 * 流程名称
	 */
	private String processName;
	
	/**
	 * 开始节点信息
	 */
	private BpmnStartEvent startEvent;
	
	/**
	 * 结束节点信息
	 */
	private BpmnEndEvent endEvent;
	
	/**
	 * 任务信息
	 */
	private List<BpmnUserTask> usertaskList;
	
	/**
	 * 连接线信息
	 */
	private List<BpmnSequenceFlow> flowList;
	
	/**
	 * 并行网关
	 */
	private List<BpmnParallelGateWay> parGateWayList;
	
	/**
	 * 排他网关
	 */
	private List<BpmnExclusiveGateway> exclusiveGatewayList;
	
	/**
	 * 坐标信息
	 */
	private List<BpmnGraphInfo> graphList;
	

	public List<BpmnParallelGateWay> getParGateWayList() {
		return parGateWayList;
	}

	public void setParGateWayList(List<BpmnParallelGateWay> parGateWayList) {
		this.parGateWayList = parGateWayList;
	}

	public List<BpmnExclusiveGateway> getExclusiveGatewayList() {
		return exclusiveGatewayList;
	}

	public void setExclusiveGatewayList(List<BpmnExclusiveGateway> exclusiveGatewayList) {
		this.exclusiveGatewayList = exclusiveGatewayList;
	}

	public List<BpmnGraphInfo> getGraphList() {
		return graphList;
	}

	public void setGraphList(List<BpmnGraphInfo> graphList) {
		this.graphList = graphList;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public BpmnStartEvent getStartEvent() {
		return startEvent;
	}

	public void setStartEvent(BpmnStartEvent startEvent) {
		this.startEvent = startEvent;
	}

	public BpmnEndEvent getEndEvent() {
		return endEvent;
	}

	public void setEndEvent(BpmnEndEvent endEvent) {
		this.endEvent = endEvent;
	}

	public List<BpmnUserTask> getUsertaskList() {
		return usertaskList;
	}

	public void setUsertaskList(List<BpmnUserTask> usertaskList) {
		this.usertaskList = usertaskList;
	}

	public List<BpmnSequenceFlow> getFlowList() {
		return flowList;
	}

	public void setFlowList(List<BpmnSequenceFlow> flowList) {
		this.flowList = flowList;
	}

}
