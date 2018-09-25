package com.yidouinc.ydl.workflow.api;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.yidouinc.mars.response.OperResult;
import com.yidouinc.ydl.workflow.dto.ActAuditBranchDto;
import com.yidouinc.ydl.workflow.dto.ActBranchApplicantDto;
import com.yidouinc.ydl.workflow.query.ActConfigQuery;

@FeignClient("ydl-workflow-service")
public interface ActAuditBranchApi {

	/**
	 * 保存审批分支
	 * 
	 * @param auditBranchDto
	 * @return
	 */
	@RequestMapping(value = "saveAuditBranch", method = RequestMethod.POST, produces = "application/json")
	public OperResult saveAuditBranch(ActAuditBranchDto auditBranchDto);

	/**
	 * 更新审批分支
	 * 
	 * @param auditBranchDto
	 * @return
	 */
	@RequestMapping(value = "updateAuditBranch", method = RequestMethod.POST, produces = "application/json")
	public OperResult updateAuditBranch(ActAuditBranchDto auditBranchDto);

	/**
	 * 更新审批分支名称
	 * 
	 * @param auditBranchDto
	 * @return
	 */
	@RequestMapping(value = "updateAuditBranchName", method = RequestMethod.POST, produces = "application/json")
	public OperResult updateAuditBranchName(ActAuditBranchDto auditBranchDto);
	
	/**
	 * 更新审批分支前置条件
	 * 
	 * @param auditBranchDto
	 * @return
	 */
	@RequestMapping(value = "updateAuditBranchCondition", method = RequestMethod.POST, produces = "application/json")
	public OperResult updateAuditBranchCondition(ActAuditBranchDto auditBranchDto);
	
	/**
	 * 更新审批分支审批人
	 * 
	 * @param auditBranchDto
	 * @return
	 */
	@RequestMapping(value = "updateAuditBranchAuditor", method = RequestMethod.POST, produces = "application/json")
	public OperResult updateAuditBranchAuditor(ActAuditBranchDto auditBranchDto);
	
	/**
	 * 更新审批分支抄送人
	 * 
	 * @param auditBranchDto
	 * @return
	 */
	@RequestMapping(value = "updateAuditBranchCc", method = RequestMethod.POST, produces = "application/json")
	public OperResult updateAuditBranchCc(ActAuditBranchDto auditBranchDto);
	
	/**
	 * 查询审批分支列表
	 * 
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "queryAuditBranchList", method = RequestMethod.POST, produces = "application/json")
	public List<ActAuditBranchDto> queryAuditBranchList(ActConfigQuery query);

	/**
	 * 根据申请人查询可用审批分支
	 * 
	 * @param startUserId
	 * @param moduleType
	 * @param companyId
	 * @return
	 */
	@RequestMapping(value = "queryValidAuditBranch", method = RequestMethod.GET)
	public ActBranchApplicantDto queryValidAuditBranch(@RequestParam("startUserId") long startUserId, @RequestParam("moduleType") String moduleType,
			@RequestParam("companyId") long companyId);
	
	/**
	 * 查询审批配置状态
	 * 
	 * @param moduleType
	 * @param companyId
	 * @return
	 */
	@RequestMapping(value = "validDefaultBranchExist", method = RequestMethod.GET)
	public boolean validDefaultBranchExist(@RequestParam("moduleType") String moduleType,@RequestParam("companyId") long companyId);
}
