package com.yidouinc.ydl.workflow.api;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.yidouinc.mars.response.OperResult;
import com.yidouinc.ydl.workflow.dto.ActAuditConfigDto;
import com.yidouinc.ydl.workflow.query.ActConfigQuery;

@FeignClient("ydl-workflow-service")
public interface ActAuditConfigApi {

	/**
	 * 保存审批配置
	 * 
	 * @param auditConfigDto
	 * @return
	 */
	@RequestMapping(value = "saveAuditConfig", method = RequestMethod.POST, produces = "application/json")
	public OperResult saveAuditConfig(ActAuditConfigDto auditConfigDto);

	/**
	 * 查询审批配置列表
	 * 
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "queryAuditConfigList", method = RequestMethod.POST, produces = "application/json")
	public List<ActAuditConfigDto> queryAuditConfigList(ActConfigQuery query);
	
	/**
	 * 查询审批配置状态
	 * 
	 * @param moduleType
	 * @param companyId
	 * @return
	 */
	@RequestMapping(value = "queryAuditConfigStautus", method = RequestMethod.GET)
	public boolean queryAuditConfigStautus(@RequestParam("moduleType") String moduleType,@RequestParam("companyId") long companyId);
}
