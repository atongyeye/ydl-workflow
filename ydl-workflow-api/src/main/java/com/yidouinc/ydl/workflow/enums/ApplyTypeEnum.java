package com.yidouinc.ydl.workflow.enums;

/**
 * 
 * @author angq 申请者类型0:用户,1:部门
 *
 */
public enum ApplyTypeEnum {

	APPLY_USER(0),
	APPLY_DEPT(1);

	private int value;

	private ApplyTypeEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}


	public void setValue(int value) {
		this.value = value;
	}


}
