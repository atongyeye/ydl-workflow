package com.yidouinc.ydl.workflow.enums;


/**
 * 
 * @author wangshuai
 *
 */
public enum AuditConfigEnum {

	CONTRACTEFFECT(0),//合同生效
	CCONTRACTEDIT(1),//合同修改
	CCONTRACTBORROW(2),//合同修改
	
	ENABLESTATUS(0),//启用
	DISENABLESTATUS(1),//禁用
	
	OFFERAPPROVE(4);//报价特价审批
	
	private int value;
	
	private AuditConfigEnum(int value){
		this.value = value;
	}
	
	public int getValue(){
		return value;
	}
}
