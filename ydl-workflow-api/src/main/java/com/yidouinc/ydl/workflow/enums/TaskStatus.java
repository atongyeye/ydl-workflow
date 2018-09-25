package com.yidouinc.ydl.workflow.enums;

/**
 * 任务状态
 * @author angq
 *
 */
public enum TaskStatus {
	/**
	 * 0:xx审批中,1:审批通过,2:审批驳回,3:已撤销
	 */
	TODO("0"),DONE("1"),REJECT("2"),CANCLE("3");
	
	private String value;
	
	private TaskStatus(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value; 
	}
}
