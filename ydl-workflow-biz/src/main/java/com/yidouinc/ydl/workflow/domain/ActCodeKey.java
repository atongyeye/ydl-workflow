package com.yidouinc.ydl.workflow.domain;

public class ActCodeKey {
    private Long companyId;

    private Integer sequenceCode;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Integer getSequenceCode() {
        return sequenceCode;
    }

    public void setSequenceCode(Integer sequenceCode) {
        this.sequenceCode = sequenceCode;
    }
}