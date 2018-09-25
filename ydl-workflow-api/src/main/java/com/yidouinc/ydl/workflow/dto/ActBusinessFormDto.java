package com.yidouinc.ydl.workflow.dto;

import java.io.Serializable;
import java.util.Date;

public class ActBusinessFormDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7987088021739799721L;

	private Long id;

    private Long companyId;

    private Long operatingFormId;

    private String procInstId;

    private Long creatorId;

    private Date createTime;

    private byte[] businessForm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getOperatingFormId() {
        return operatingFormId;
    }

    public void setOperatingFormId(Long operatingFormId) {
        this.operatingFormId = operatingFormId;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public byte[] getBusinessForm() {
        return businessForm;
    }

    public void setBusinessForm(byte[] businessForm) {
        this.businessForm = businessForm;
    }
}