package com.yidouinc.ydl.workflow.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author angq 流程操作类型
 *
 */
public enum OperatingType {

	/**
	 * 1:审批中,2:发起申请,3:已同意申请,4:已驳回申请,5:重新发起申请,6:发表了评论,7:已撤销申请
	 */
	PENDING(1, "审批中"),
	START(2, "发起申请"),
	PASS(3, "已同意申请"),
	REJECT(4, "已驳回申请"),
	RE_START(5, "重新发起申请"),
	COMMENT(6, "发表了评论"),
	CANCLE(7, "已撤销申请");

	private int value;
	private String desc;

	private OperatingType(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public int getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static Map<Integer, String> getInformations() {
		Map<Integer, String> result = new HashMap<Integer, String>();
		for (OperatingType e : OperatingType.values()) {
			result.put(e.getValue(), e.getDesc());
		}
		return result;
	}

	public static String getDesc(int value) {
		for (OperatingType e : OperatingType.values()) {
			if (e.getValue() == value) {
				return e.getDesc();
			}
		}
		return "";
	}
}
