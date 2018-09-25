package com.yidouinc.ydl.workflow.dto;

import java.io.Serializable;

public class SequenceFlowDto implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6098687659422494807L;

	private String id;
	
	private String sourceRef;
	
	private String targetRef;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSourceRef() {
		return sourceRef;
	}

	public void setSourceRef(String sourceRef) {
		this.sourceRef = sourceRef;
	}

	public String getTargetRef() {
		return targetRef;
	}

	public void setTargetRef(String targetRef) {
		this.targetRef = targetRef;
	}
	
	

}