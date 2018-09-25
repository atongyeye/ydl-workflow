package com.yidouinc.ydl.workflow.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.yidouinc.ydl.workflow.api.ActOperatingApi;
import com.yidouinc.ydl.workflow.service.ActOperatingService;

@RestController
public class ActOperatingApiImp implements ActOperatingApi {

	@Autowired
	private ActOperatingService actOperatingService;

	@Override
	public boolean queryApprovedModuleExist(long moduleId, String moduleType, long companyId) {
		return actOperatingService.queryApprovedModuleExist(moduleId, moduleType, companyId);
	}

	@Override
	public String queryProcInstIdByModuleId(long moduleId, String moduleType, long companyId) {
		return actOperatingService.queryProcInstIdByModuleId(moduleId, moduleType, companyId);
	}

}
