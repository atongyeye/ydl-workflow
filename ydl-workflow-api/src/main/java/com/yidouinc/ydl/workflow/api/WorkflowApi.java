package com.yidouinc.ydl.workflow.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.yidouinc.mars.response.OperResult;
import com.yidouinc.ydl.workflow.dto.ProcDefDto;
import com.yidouinc.ydl.workflow.dto.ProcInstanceDto;
import com.yidouinc.ydl.workflow.dto.ProcessDetailDto;
import com.yidouinc.ydl.workflow.dto.TaskDto;
import com.yidouinc.ydl.workflow.query.WorkflowQuery;
import com.yidouinc.ydl.workflow.response.ProcBranchResponse;

@FeignClient("ydl-workflow-service")
public interface WorkflowApi {

	/**
	 * 启动流程实例
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/startWorkflow", method = RequestMethod.POST, produces = "application/json")
	public String startWorkflow(ProcInstanceDto dto);

	/**
	 * 执行任务
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/completeTask", method = RequestMethod.POST, produces = "application/json")
	public OperResult completeTask(TaskDto dto);

	/**
	 * 流程驳回pc
	 * 
	 * @param dto
	 */
	@RequestMapping(value = "/rejectProcess", method = RequestMethod.POST, produces = "application/json")
	public OperResult rejectProcess(TaskDto dto);

	/**
	 * 流程撤回
	 * 
	 * @param dto
	 */
	@RequestMapping(value = "/cancleProcess", method = RequestMethod.POST, produces = "application/json")
	public OperResult cancleProcess(TaskDto dto);

	/**
	 * 添加评论
	 * 
	 * @param dto
	 */
	@RequestMapping(value = "/addCommentForProcess", method = RequestMethod.POST, produces = "application/json")
	public void addCommentForProcess(ProcInstanceDto dto);

	/**
	 * 查询我审批的任务列表
	 * 
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "/queryMyAuditTaskPage", method = RequestMethod.POST, produces = "application/json")
	public PageInfo<ProcInstanceDto> queryMyAuditTaskPage(@RequestBody WorkflowQuery query);

	/**
	 * 查询我发起的流程实例列表
	 * 
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "/getMyApplyTasks", method = RequestMethod.POST, produces = "application/json")
	public PageInfo<ProcInstanceDto> getMyApplyTasks(WorkflowQuery query);

	/**
	 * 查询抄送给我的流程实例列表
	 * 
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "/getMyCcTasks", method = RequestMethod.POST, produces = "application/json")
	public PageInfo<ProcInstanceDto> getMyCcTasks(WorkflowQuery query);

	/**
	 * 查询我的待办审批数量
	 * 
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "/getMyTodoCount", method = RequestMethod.POST, produces = "application/json")
	public long getMyTodoCount(WorkflowQuery query);

	/**
	 * 查询审批信息
	 * 
	 * @param procInstId
	 * @param companyId
	 * @return
	 */
	@RequestMapping(value = "/getProcessDetail", method = RequestMethod.GET)
	public ProcessDetailDto getProcessDetail(@RequestParam("procInstId") String procInstId, @RequestParam("companyId") long companyId);

	/**
	 * 获取流程定义元素
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/getFlowElements", method = RequestMethod.POST, produces = "application/json")
	public String getFlowElements(ProcDefDto dto);

	/**
	 * 查询流程审批人，抄送人
	 * 
	 * @param procDefKey
	 * @param companyId
	 * @return
	 */
	@RequestMapping(value = "/getProcBranchUsers", method = RequestMethod.GET)
	public ProcBranchResponse getProcBranchUsers(@RequestParam("procDefKey") String procDefKey, @RequestParam("companyId") long companyId);
}
