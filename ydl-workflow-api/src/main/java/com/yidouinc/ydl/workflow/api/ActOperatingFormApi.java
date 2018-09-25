package com.yidouinc.ydl.workflow.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.yidouinc.ydl.workflow.dto.ActOperatingFormDto;

@FeignClient("ydl-workflow-service")
public interface ActOperatingFormApi {

	
	/**
	 * 判断模块是否已在审批中
	 * 
	 * @param moduleId
	 * @param moduleType
	 * @param companyId
	 * @return
	 */
	@RequestMapping(value = "queryOperatingForm", method = RequestMethod.GET)
	public ActOperatingFormDto queryOperatingForm(@RequestParam("moduleId") long moduleId,@RequestParam("moduleType") String moduleType,@RequestParam("companyId") long companyId);
}
