package com.yidouinc.ydl.workflow.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.yidouinc.mars.response.OperResult;
import com.yidouinc.ydl.workflow.api.ActAuditBranchApi;
import com.yidouinc.ydl.workflow.dto.ActAuditBranchDto;
import com.yidouinc.ydl.workflow.dto.ActBranchApplicantDto;
import com.yidouinc.ydl.workflow.query.ActConfigQuery;
import com.yidouinc.ydl.workflow.service.ActAuditBranchService;

@RestController
public class ActAuditBranchApiImp implements ActAuditBranchApi {

	@Autowired
	private ActAuditBranchService auditBranchService;

	@Override
	public OperResult saveAuditBranch(@RequestBody ActAuditBranchDto auditBranchDto) {
		return auditBranchService.saveActAuditBranch(auditBranchDto);
	}

	@Override
	public OperResult updateAuditBranch(@RequestBody ActAuditBranchDto auditBranchDto) {
		return auditBranchService.updateActAuditBranch(auditBranchDto);
	}

	@Override
	public OperResult updateAuditBranchName(@RequestBody ActAuditBranchDto auditBranchDto) {
		return auditBranchService.updateActAuditBranchName(auditBranchDto);
	}
	
	@Override
	public OperResult updateAuditBranchCondition(@RequestBody ActAuditBranchDto auditBranchDto) {
		return auditBranchService.updateActAuditBranchCondition(auditBranchDto);
	}
	
	@Override
	public OperResult updateAuditBranchAuditor(@RequestBody ActAuditBranchDto auditBranchDto) {
		return auditBranchService.updateActAuditBranchAuditor(auditBranchDto);
	}
	
	@Override
	public OperResult updateAuditBranchCc(@RequestBody ActAuditBranchDto auditBranchDto) {
		return auditBranchService.updateActAuditBranchCc(auditBranchDto);
	}
	
	@Override
	public List<ActAuditBranchDto> queryAuditBranchList(@RequestBody ActConfigQuery query) {
		return auditBranchService.queryAuditBranchList(query);
	}

	@Override
	public ActBranchApplicantDto queryValidAuditBranch(long startUserId, String moduleType, long companyId) {
		return auditBranchService.queryValidAuditBranch(startUserId, moduleType, companyId);
	}

	@Override
	public boolean validDefaultBranchExist(String moduleType, long companyId) {
		return auditBranchService.validDefaultBranchExist(moduleType, companyId);
	}

}
