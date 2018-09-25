package com.yidouinc.ydl.workflow.imp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.yidouinc.ydl.workflow.api.WorkflowRepositoryApi;
import com.yidouinc.ydl.workflow.bpmnDto.BpmnModleDto;
import com.yidouinc.ydl.workflow.dto.ProcDefDto;
import com.yidouinc.ydl.workflow.dto.ProcInstanceDto;
import com.yidouinc.ydl.workflow.query.WorkflowQuery;
import com.yidouinc.ydl.workflow.service.BpmnProduceService;
import com.yidouinc.ydl.workflow.service.WorkflowRepositoryService;
import com.yidouinc.ydl.workflow.service.WorkflowService;

@RestController
public class WorkflowRepositoryApiImp implements WorkflowRepositoryApi {

	@Autowired
	WorkflowService workflowService;
	@Autowired
	WorkflowRepositoryService wfRepositoryService;

	@Autowired
	BpmnProduceService bpmnService;

	@Override
	public String deployProcess(@RequestBody ProcDefDto dto) {
		return wfRepositoryService.deployProcess(dto);
	}

	@Override
	public void deployProcessFile(String filePath, String fileName,Long companyId) {
		wfRepositoryService.deployProcessFile(filePath, fileName,companyId);
	}

	@Override
	public PageInfo<ProcDefDto> queryProcessList(@RequestBody WorkflowQuery query) {

		return wfRepositoryService.queryProcessList(query);
	}

	@Override
	public void deleteProcesses(String ids, long userId) {
		wfRepositoryService.delDeployment(ids);

	}

	@Override
	public void removeDeployment(String procDefId) {
		wfRepositoryService.removeDeployment(procDefId);

	}
	
	@Override
	public byte[] getProcessInstanceDiagram(@RequestBody ProcInstanceDto dto) {
		return wfRepositoryService.getProcessInstanceDiagram(dto);
	}

	@Override
	public List<Map<String, String>> getWorkFlowList(Long companyId) {
		return wfRepositoryService.getWorkFlowList(companyId);
	}

	@Override
	public PageInfo<ProcDefDto> getWorkFlowMyPage(@RequestBody WorkflowQuery query) {
		return wfRepositoryService.getWorkFlowMyPage(query);
	}

	@Override
	public void produceBpmnJson(@RequestBody BpmnModleDto modelDto, Long companyId) {
		bpmnService.produceBpmnJson(modelDto, companyId);
	}
}
