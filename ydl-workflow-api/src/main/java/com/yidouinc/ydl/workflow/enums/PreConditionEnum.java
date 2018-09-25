package com.yidouinc.ydl.workflow.enums;

/**
 * 
 * @author angq 审批分支前置条件
 *
 */
public enum PreConditionEnum {

	/**
	 * 0:无,1:有
	 */
	NO_CONDITION(0),
	HAS_CONDITION(1);

	private int value;

	private PreConditionEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}


	public void setValue(int value) {
		this.value = value;
	}


}
