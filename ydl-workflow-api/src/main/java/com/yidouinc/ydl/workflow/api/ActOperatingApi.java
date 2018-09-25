package com.yidouinc.ydl.workflow.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("ydl-workflow-service")
public interface ActOperatingApi {

	
	/**
	 * 判断模块是否已在审批中
	 * 
	 * @param moduleId
	 * @param moduleType
	 * @param companyId
	 * @return
	 */
	@RequestMapping(value = "queryApprovedModuleExist", method = RequestMethod.GET)
	public boolean queryApprovedModuleExist(@RequestParam("moduleId") long moduleId,@RequestParam("moduleType") String moduleType,@RequestParam("companyId") long companyId);

	/**
	 * 根据模块id查询流程实例id
	 * 
	 * @param moduleId
	 * @param moduleType
	 * @param companyId
	 * @return
	 */
	@RequestMapping(value = "queryProcInstIdByModuleId", method = RequestMethod.GET)
	public String queryProcInstIdByModuleId(@RequestParam("moduleId") long moduleId,@RequestParam("moduleType") String moduleType,@RequestParam("companyId") long companyId);
}
