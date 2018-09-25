package com.yidouinc.ydl.workflow.api;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.yidouinc.ydl.workflow.bpmnDto.BpmnModleDto;
import com.yidouinc.ydl.workflow.dto.ProcDefDto;
import com.yidouinc.ydl.workflow.dto.ProcInstanceDto;
import com.yidouinc.ydl.workflow.query.WorkflowQuery;

@FeignClient("ydl-workflow-service")
public interface WorkflowRepositoryApi {

	/**
	 * 流程部署
	 * 
	 * @param deployPath
	 * @return
	 * @return String 返回类型
	 */
	@RequestMapping(value = "/deployProcess", method = RequestMethod.POST, produces = "application/json")
	public String deployProcess(ProcDefDto dto);

	/**
	 * 流程文件部署
	 * 
	 * @param filePath
	 * @param fileName
	 * @param companyId
	 */
	@RequestMapping(value = "/deployProcessFile", method = RequestMethod.POST, produces = "application/json")
	public void deployProcessFile(@RequestParam(value = "filePath") String filePath, @RequestParam(value = "fileName") String fileName,
			@RequestParam(value = "companyId") Long companyId);

	/**
	 * 分页查询业务类型列表
	 * 
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "/queryProcessList", method = RequestMethod.POST, produces = "application/json")
	public PageInfo<ProcDefDto> queryProcessList(WorkflowQuery query);

	/**
	 * 批量删除流程
	 * 
	 * @param ids
	 */
	@RequestMapping(value = "/deleteProcesses", method = RequestMethod.GET, produces = "application/json")
	public void deleteProcesses(@RequestParam("ids") String ids, @RequestParam("userId") long userId);

	/**
	 * 物理删除流程
	 * 
	 * @param procDefId
	 */
	@RequestMapping(value = "/removeDeployment", method = RequestMethod.POST, produces = "application/json")
	public void removeDeployment(@RequestParam("procDefId") String procDefId);
	/**
	 * 查看流程图
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/getProcessInstanceDiagram", method = RequestMethod.POST, produces = "application/json")
	public byte[] getProcessInstanceDiagram(ProcInstanceDto dto);

	@RequestMapping(value = "/getWorkFlowList", method = RequestMethod.POST, produces = "application/json")
	public List<Map<String, String>> getWorkFlowList(@RequestParam("companyId") Long companyId);

	/**
	 * 分页查询流程列表
	 * 
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "/getWorkFlowMyPage", method = RequestMethod.POST, produces = "application/json")
	public PageInfo<ProcDefDto> getWorkFlowMyPage(WorkflowQuery query);

	/**
	 * * 生成bpmn文件
	 * 
	 * @param modelDto
	 */
	@RequestMapping(value = "/produceBpmnJson", method = RequestMethod.POST, produces = "application/json")
	public void produceBpmnJson(@RequestBody BpmnModleDto modelDto, @RequestParam("companyId") Long companyId);

	
}
