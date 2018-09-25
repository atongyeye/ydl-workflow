package com.yidouinc.ydl.workflow.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.yidouinc.ydl.workflow.api.ActOperatingFormApi;
import com.yidouinc.ydl.workflow.dto.ActOperatingFormDto;
import com.yidouinc.ydl.workflow.service.ActOperatingFormService;

@RestController
public class ActOperatingFormApiImp implements ActOperatingFormApi {

	@Autowired
	private ActOperatingFormService actOperatingFormService;

	@Override
	public ActOperatingFormDto queryOperatingForm(long moduleId, String moduleType, long companyId) {
		return actOperatingFormService.queryOperatingForm(moduleId, moduleType, companyId);
	}



}
