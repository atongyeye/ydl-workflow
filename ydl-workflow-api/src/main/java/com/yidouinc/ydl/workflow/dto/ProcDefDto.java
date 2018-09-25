package com.yidouinc.ydl.workflow.dto;

import java.io.Serializable;

public class ProcDefDto implements Serializable {

	/** 
	 */
	private static final long serialVersionUID = 4935113431744584764L;

	private String id;

	private String name;
	
	private String key;
	
	private int version;
	
	private String deployPath;
	
	private Long companyId;
	
	private String deploymentId;
	
	private String userId;
	
	private String description;
	
	private long instanceCount;
	
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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getDeployPath() {
		return deployPath;
	}

	public void setDeployPath(String deployPath) {
		this.deployPath = deployPath;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getInstanceCount() {
		return instanceCount;
	}

	public void setInstanceCount(long instanceCount) {
		this.instanceCount = instanceCount;
	}

	

	
}
