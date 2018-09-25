package com.yidouinc.ydl.workflow.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.yidouinc.mars.response.OperResult;
import com.yidouinc.ydl.workflow.api.WorkflowApi;
import com.yidouinc.ydl.workflow.dto.ProcDefDto;
import com.yidouinc.ydl.workflow.dto.ProcInstanceDto;
import com.yidouinc.ydl.workflow.dto.ProcessDetailDto;
import com.yidouinc.ydl.workflow.dto.TaskDto;
import com.yidouinc.ydl.workflow.query.WorkflowQuery;
import com.yidouinc.ydl.workflow.response.ProcBranchResponse;
import com.yidouinc.ydl.workflow.service.ActCcPersonService;
import com.yidouinc.ydl.workflow.service.ActOperatingService;
import com.yidouinc.ydl.workflow.service.BpmnProduceService;
import com.yidouinc.ydl.workflow.service.WorkflowBusinessService;
import com.yidouinc.ydl.workflow.service.WorkflowRepositoryService;
import com.yidouinc.ydl.workflow.service.WorkflowService;

@RestController
public class WorkflowApiImp implements WorkflowApi {

	@Autowired
	WorkflowService workflowService;

	@Autowired
	WorkflowRepositoryService wfRepositoryService;

	@Autowired
	BpmnProduceService bpmnService;

	@Autowired
	ActCcPersonService actCcPersonService;

	@Autowired
	WorkflowBusinessService workflowBusinessService;

	@Autowired
	ActOperatingService actOperatingService;

	@Override
	public String startWorkflow(@RequestBody ProcInstanceDto dto) {
		return workflowService.startWorkflow(dto);
	}

	@Override
	public OperResult completeTask(@RequestBody TaskDto dto) {
		return workflowService.completeTask(dto);
	}

	@Override
	public OperResult rejectProcess(@RequestBody TaskDto dto) {
		return workflowService.rejectProcess(dto);
	}

	@Override
	public OperResult cancleProcess(@RequestBody TaskDto dto) {
		return workflowService.cancleProcess(dto);
	}

	@Override
	public void addCommentForProcess(@RequestBody ProcInstanceDto dto) {
		actOperatingService.saveOperatingForProcessInstance(dto);
	}

	@Override
	public PageInfo<ProcInstanceDto> queryMyAuditTaskPage(@RequestBody WorkflowQuery query) {
		return workflowService.getMyAuditTasks(query);
	}

	@Override
	public PageInfo<ProcInstanceDto> getMyApplyTasks(@RequestBody WorkflowQuery query) {
		return workflowService.getMyApplyTasks(query);
	}

	@Override
	public PageInfo<ProcInstanceDto> getMyCcTasks(@RequestBody WorkflowQuery query) {
		return workflowBusinessService.queryCcProcInstListByUserId(query);
	}

	@Override
	public long getMyTodoCount(@RequestBody WorkflowQuery query) {
		return workflowService.getMyTodoCount(query);
	}
	
	@Override
	public ProcessDetailDto getProcessDetail(String procInstId, long companyId) {
		return workflowBusinessService.getProcessDetail(procInstId, companyId);
	}

	@Override
	public String getFlowElements(@RequestBody ProcDefDto dto) {
		return workflowService.getFlowElements(dto);
	}

	@Override
	public ProcBranchResponse getProcBranchUsers(String procDefKey, long companyId) {
		return workflowService.getProcBranchUsers(procDefKey, companyId);
	}
}
