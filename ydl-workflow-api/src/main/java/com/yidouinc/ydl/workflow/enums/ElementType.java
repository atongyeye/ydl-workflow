package com.yidouinc.ydl.workflow.enums;

/**
 * 元素类型
 * @author lujianhao
 *
 */
public enum ElementType {
	
	/**
	 * 0:StartEvent,1:UserTask,2:EndEvent,3:ExclusiveGateway,4:ParallelGateway,5:SequenceFlow
	 */
	START_EVENT("0"),USER_TASK("1"),END_EVENT("2"),EXCLUSIVE("3"),PARALLEL("4"),SEQUENCE_FLOW("5");
	
	private String value;
	
	private ElementType(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value; 
	}
}
