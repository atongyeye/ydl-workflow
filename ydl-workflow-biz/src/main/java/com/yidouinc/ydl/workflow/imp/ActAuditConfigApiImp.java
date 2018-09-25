package com.yidouinc.ydl.workflow.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.yidouinc.mars.response.OperResult;
import com.yidouinc.ydl.workflow.api.ActAuditConfigApi;
import com.yidouinc.ydl.workflow.dto.ActAuditConfigDto;
import com.yidouinc.ydl.workflow.query.ActConfigQuery;
import com.yidouinc.ydl.workflow.service.ActAuditConfigService;

@RestController
public class ActAuditConfigApiImp implements ActAuditConfigApi {

	@Autowired
	private ActAuditConfigService auditConfigService;

	@Override
	public OperResult saveAuditConfig(@RequestBody ActAuditConfigDto auditConfigDto) {
		return auditConfigService.saveAuditConfig(auditConfigDto);
	}

	@Override
	public List<ActAuditConfigDto> queryAuditConfigList(@RequestBody ActConfigQuery query) {
		return auditConfigService.queryAuditConfigList(query);
	}

	@Override
	public boolean queryAuditConfigStautus(String moduleType, long companyId) {
		return auditConfigService.queryAuditConfigStautus(moduleType, companyId);
	}

}
