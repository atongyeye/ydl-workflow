/**
 * 
 */
package com.yidouinc.ydl.workflow.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.ParallelGateway;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.yidouinc.mars.common.utils.DTOConvert;
import com.yidouinc.mars.response.OperResult;
import com.yidouinc.ydl.tenant.api.CompUserApi;
import com.yidouinc.ydl.tenant.dto.CompUserDto;
import com.yidouinc.ydl.workflow.domain.ActOperating;
import com.yidouinc.ydl.workflow.dto.ActOperatingFormDto;
import com.yidouinc.ydl.workflow.dto.FlowElementDto;
import com.yidouinc.ydl.workflow.dto.ProcDefDto;
import com.yidouinc.ydl.workflow.dto.ProcInstanceDto;
import com.yidouinc.ydl.workflow.dto.SequenceFlowDto;
import com.yidouinc.ydl.workflow.dto.TaskDto;
import com.yidouinc.ydl.workflow.dto.TaskUserDto;
import com.yidouinc.ydl.workflow.enums.ElementType;
import com.yidouinc.ydl.workflow.mapper.ActBranchCcMapper;
import com.yidouinc.ydl.workflow.query.WorkflowQuery;
import com.yidouinc.ydl.workflow.response.BranchPersonDto;
import com.yidouinc.ydl.workflow.response.ProcBranchResponse;

import net.sf.json.JSONArray;

/**
 * @author angq 流程
 *
 */
@Service
@Transactional
public class WorkflowService {

	@Autowired
	private TaskService taskService;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private IdentityService identityService;

	@Autowired
	private WorkflowBusinessService workflowBusinessService;

	@Autowired
	private ActBranchCcMapper actBranchCcMapper;

	@Autowired
	private CompUserApi compUserApi;

	@Autowired
	private ActOperatingFormService actOperatingFormService;

	@Autowired
	private ActOperatingService actOperatingService;

	/**
	 * 启动流程
	 * 
	 * @param dto
	 * @return
	 */
	public String startWorkflow(ProcInstanceDto dto) {
		ProcessInstance processInstance = null;
		if (dto.getStartUserId() != null) {
			identityService.setAuthenticatedUserId(dto.getStartUserId().toString());
		}
		OperResult result = actOperatingFormService.saveOperatingForm(dto);
		dto.setOperatingFormId(result.getId());
		String companyId = String.valueOf(dto.getCompanyId());
		processInstance = runtimeService.startProcessInstanceByKeyAndTenantId(dto.getProcDefKey(), dto.getBusinessType() + "-" + dto.getBusinessKey(),
				companyId);
		dto.setProcInstId(processInstance.getId());
		workflowBusinessService.saveWorkflowRelateInfo(dto);
		return processInstance.getId();
	}

	/**
	 * 审批通过
	 * 
	 * @param dto
	 */
	public OperResult completeTask(TaskDto dto) {
		dto = setProcInstId(dto);
		if (dto == null) {
			return OperResult.getErrorResult("审批信息不存在.");
		}
		// taskService.complete(dto.getTaskId());
		List<Task> taskList = taskService.createTaskQuery().processInstanceId(dto.getProcInstId()).taskTenantId(String.valueOf(dto.getCompanyId()))
				.list();
		if (CollectionUtils.isNotEmpty(taskList)) {
			for (Task task : taskList) {
				taskService.complete(task.getId());
			}
		}
		workflowBusinessService.saveTaskCommitRelateInfo(dto);
		return OperResult.getSuccessResult();
	}

	/**
	 * 审批驳回
	 * 
	 * @param dto
	 */
	public OperResult rejectProcess(TaskDto dto) {
		dto = setProcInstId(dto);
		if (dto == null) {
			return OperResult.getErrorResult("审批信息不存在.");
		}
		List<Task> taskList = taskService.createTaskQuery().processInstanceId(dto.getProcInstId()).list();
		if (CollectionUtils.isNotEmpty(taskList)) {
			for (Task task : taskList) {
				ActivityImpl endActivity = findActivitiImpl(task.getId(), "end");
				// if (!dto.getTaskId().equals(task.getId())) {// 当前任务,审批人不置空
				// taskService.setAssignee(task.getId(), null);
				// }
				commitProcess(task.getId(), endActivity.getId(), dto);
			}
		}
		workflowBusinessService.saveTaskCommitRelateInfo(dto);
		return OperResult.getSuccessResult();
	}

	/**
	 * 审批撤销
	 * 
	 * @param dto
	 * @return
	 */
	public OperResult cancleProcess(TaskDto dto) {
		dto = setProcInstId(dto);
		if (dto == null) {
			return OperResult.getErrorResult("审批信息不存在.");
		}
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(dto.getProcInstId()).taskTenantId(String.valueOf(dto.getCompanyId()))
				.list();
		if (CollectionUtils.isNotEmpty(tasks)) {
			for (Task task : tasks) {
				ActivityImpl endActivity = findActivitiImpl(task.getId(), "end");
				taskService.setAssignee(task.getId(), null);// 撤销,设置未执行任务的审批人为空
				commitProcess(task.getId(), endActivity.getId(), dto);
			}
			workflowBusinessService.saveTaskCommitRelateInfo(dto);
		}
		return OperResult.getSuccessResult();
	}

	/**
	 * 设置流程实例id
	 * 
	 * @param dto
	 * @return
	 */
	private TaskDto setProcInstId(TaskDto dto) {
		// 获取流程实例id
		ActOperatingFormDto operatingForm = actOperatingFormService.queryOperatingForm(Long.valueOf(dto.getBusinessKey()), dto.getBusinessType(),
				dto.getCompanyId());
		if (operatingForm == null) {
			return null;
		} else {
			ActOperating operating = actOperatingService.queryOperatingForApproved(operatingForm.getId(), dto.getCompanyId());
			if (operating == null) {
				return null;
			} else {
				dto.setProcInstId(operating.getProcInstId());
			}
		}
		return dto;
	}

	/**
	 * 查询我审批的任务列表(待审批,已审批)
	 * 
	 * @param query
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PageInfo<ProcInstanceDto> getMyAuditTasks(WorkflowQuery query) {
		HistoricTaskInstanceQuery taskQuery = historyService.createHistoricTaskInstanceQuery();// 创建历史任务实例查询
		if (query.getUserId() != null) {
			taskQuery.taskAssignee(String.valueOf(query.getUserId()));
		}
		if (query.getCompanyId() != null) {
			taskQuery.taskTenantId(String.valueOf(query.getCompanyId()));
		}
		List<HistoricTaskInstance> hisTaskList = taskQuery.orderByHistoricTaskInstanceEndTime().asc().orderByTaskId().desc()
				.listPage((query.getCurrentPage() - 1) * query.getPageSize(), query.getPageSize());
		List<ProcInstanceDto> taskList = new ArrayList<ProcInstanceDto>();
		if (CollectionUtils.isNotEmpty(hisTaskList)) {
			for (HistoricTaskInstance task : hisTaskList) {
				ProcInstanceDto dto = new ProcInstanceDto();
				dto.setProcInstId(task.getProcessInstanceId());
				dto.setStartTime(task.getCreateTime());
				dto.setCompanyId(query.getCompanyId());
				dto = workflowBusinessService.setTaskDtoForOperating(dto);// 回填任务信息
				taskList.add(dto);
			}
		}
		PageInfo page = new PageInfo(taskList);
		int total = (int) taskQuery.count();// 查询任务数
		page.setTotal(total);
		page.setPageNum(query.getCurrentPage());
		return page;
	}

	/**
	 * 查询我发起的流程实例
	 * 
	 * @param query
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PageInfo<ProcInstanceDto> getMyApplyTasks(WorkflowQuery query) {
		HistoricProcessInstanceQuery hisQuery = historyService.createHistoricProcessInstanceQuery();
		if (query.getUserId() != null) {
			hisQuery.startedBy(String.valueOf(query.getUserId()));
		}
		if (query.getCompanyId() != null) {
			hisQuery.processInstanceTenantId(String.valueOf(query.getCompanyId()));
		}
		List<HistoricProcessInstance> piList = hisQuery.orderByProcessInstanceStartTime().desc()
				.listPage((query.getCurrentPage() - 1) * query.getPageSize(), query.getPageSize());
		List<ProcInstanceDto> procInstanceList = new ArrayList<ProcInstanceDto>();
		if (CollectionUtils.isNotEmpty(piList)) {
			for (HistoricProcessInstance hisPi : piList) {
				ProcInstanceDto piDto = new ProcInstanceDto();
				piDto.setBusinessKey(hisPi.getBusinessKey());
				piDto.setProcDefKey(hisPi.getProcessDefinitionKey());
				piDto.setProcInstId(hisPi.getId());
				piDto.setStartTime(hisPi.getStartTime());
				piDto.setStartUserId(Long.valueOf(hisPi.getStartUserId()));
				piDto.setCompanyId(query.getCompanyId());
				piDto = workflowBusinessService.setProcInstDtoForOperating(piDto);// 回填流程实例信息
				procInstanceList.add(piDto);
			}
		}
		PageInfo page = new PageInfo(procInstanceList);
		page.setTotal((int) hisQuery.count());
		page.setPageNum(query.getCurrentPage());
		return page;
	}

	/**
	 * 查询我的待办审批数量
	 * 
	 * @param query
	 * @return
	 */
	public long  getMyTodoCount(WorkflowQuery query) {
		TaskQuery taskQuery = taskService.createTaskQuery();
		return taskQuery.taskAssignee(String.valueOf(query.getUserId())).taskTenantId(String.valueOf(query.getCompanyId())).count();
	}
	
	/**
	 * @param taskId
	 * @param activityId
	 *            流程转向执行任务节点ID<br>
	 *            此参数为空，默认为提交操作
	 */
	private void commitProcess(String taskId, String activityId, TaskDto dto) {
		// 跳转节点为空，默认提交操作
		if (StringUtils.isBlank(activityId)) {
			taskService.complete(taskId);
		} else {// 流程转向操作
			turnEndTransition(taskId, activityId, dto);
		}
	}

	/**
	 * 清空指定活动节点流向
	 * 
	 * @param activityImpl
	 *            活动节点
	 * @return 节点流向集合
	 */
	private List<PvmTransition> clearTransition(ActivityImpl activityImpl) {
		// 存储当前节点所有流向临时变量
		List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
		// 获取当前节点所有流向，存储到临时变量，然后清空
		List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
		for (PvmTransition pvmTransition : pvmTransitionList) {
			oriPvmTransitionList.add(pvmTransition);
		}
		pvmTransitionList.clear();

		return oriPvmTransitionList;
	}

	/**
	 * 还原指定活动节点流向
	 * 
	 * @param activityImpl
	 *            活动节点
	 * @param oriPvmTransitionList
	 *            原有节点流向集合
	 */
	private void restoreTransition(ActivityImpl activityImpl, List<PvmTransition> oriPvmTransitionList) {
		// 清空现有流向
		List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
		pvmTransitionList.clear();
		// 还原以前流向
		for (PvmTransition pvmTransition : oriPvmTransitionList) {
			pvmTransitionList.add(pvmTransition);
		}
	}

	/**
	 * 流程转向操作
	 * 
	 * @param taskId
	 * @param activityId
	 *            目标节点任务ID
	 * @param variables
	 */
	@SuppressWarnings("unused")
	private void turnTransition(String taskId, String activityId, Map<String, Object> variables) {
		// 当前节点
		ActivityImpl currActivity = findActivitiImpl(taskId, null);
		// 清空当前流向
		List<PvmTransition> oriPvmTransitionList = clearTransition(currActivity);
		// 创建新流向
		TransitionImpl newTransition = currActivity.createOutgoingTransition();
		// 目标节点
		ActivityImpl pointActivity = findActivitiImpl(taskId, activityId);
		// 设置新流向的目标节点
		newTransition.setDestination(pointActivity);
		// 执行转向任务
		taskService.complete(taskId, variables, true);
		// 删除目标节点新流入
		pointActivity.getIncomingTransitions().remove(newTransition);
		// 还原以前流向
		restoreTransition(currActivity, oriPvmTransitionList);
	}

	/**
	 * 流程转向操作
	 * 
	 * @param taskId
	 * @param activityId
	 *            目标节点任务ID
	 * @param variables
	 */
	private void turnEndTransition(String taskId, String activityId, TaskDto dto) {
		// 当前节点
		ActivityImpl currActivity = findActivitiImpl(taskId, null);
		// 清空当前流向
		List<PvmTransition> oriPvmTransitionList = clearTransition(currActivity);
		// 创建新流向
		TransitionImpl newTransition = currActivity.createOutgoingTransition();
		// 目标节点
		ActivityImpl pointActivity = findActivitiImpl(taskId, activityId);
		// 设置新流向的目标节点
		newTransition.setDestination(pointActivity);
		// 执行转向任务
		taskService.complete(taskId);
		// 删除目标节点新流入
		pointActivity.getIncomingTransitions().remove(newTransition);
		// 还原以前流向
		restoreTransition(currActivity, oriPvmTransitionList);
	}

	/**
	 * 根据任务ID获得任务实例
	 * 
	 * @param taskId
	 * @return
	 */
	private TaskEntity findTaskById(String taskId) {
		TaskEntity task = (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
		return task;
	}

	/**
	 * 根据任务ID获取流程定义
	 * 
	 * @param taskId
	 * @return
	 */
	private ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(String taskId) {
		// 取得流程定义
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(findTaskById(taskId).getProcessDefinitionId());

		return processDefinition;
	}

	/**
	 * 根据任务ID和节点ID获取活动节点 <br>
	 * 
	 * @param taskId
	 * @param activityId
	 *            活动节点ID <br>
	 *            如果为null或""，则默认查询当前活动节点 <br>
	 *            如果为"end"，则查询结束节点 <br>
	 * 
	 * @return
	 */
	private ActivityImpl findActivitiImpl(String taskId, String activityId) {
		// 取得流程定义
		ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId);

		// 获取当前活动节点ID
		if (StringUtils.isBlank(activityId)) {
			activityId = findTaskById(taskId).getTaskDefinitionKey();
		}
		// 根据流程定义，获取该流程实例的结束节点
		if (activityId.toUpperCase().equals("END")) {
			for (ActivityImpl activityImpl : processDefinition.getActivities()) {
				List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
				if (pvmTransitionList.isEmpty()) {
					return activityImpl;
				}
			}
		}
		// 根据节点ID，获取对应的活动节点
		ActivityImpl activityImpl = ((ProcessDefinitionImpl) processDefinition).findActivity(activityId);

		return activityImpl;
	}

	private List<SequenceFlowDto> getIncomingList(List<SequenceFlow> dataInList) {
		List<SequenceFlowDto> incomingList = new ArrayList<SequenceFlowDto>();
		if (CollectionUtils.isNotEmpty(dataInList)) {
			for (SequenceFlow dataIn : dataInList) {
				SequenceFlowDto incoming = new SequenceFlowDto();
				incoming.setId(dataIn.getId());
				incoming.setSourceRef(dataIn.getSourceRef());
				incoming.setTargetRef(dataIn.getTargetRef());
				incomingList.add(incoming);
			}
		}
		return incomingList;
	}

	private List<SequenceFlowDto> getOutgoingList(List<SequenceFlow> dataOutList) {
		List<SequenceFlowDto> outgoingList = new ArrayList<SequenceFlowDto>();
		if (CollectionUtils.isNotEmpty(dataOutList)) {
			for (SequenceFlow dataOut : dataOutList) {
				SequenceFlowDto outgoing = new SequenceFlowDto();
				outgoing.setId(dataOut.getId());
				outgoing.setSourceRef(dataOut.getSourceRef());
				outgoing.setTargetRef(dataOut.getTargetRef());
				outgoingList.add(outgoing);
			}
		}
		return outgoingList;
	}

	/**
	 * 查询流程元素列表
	 * 
	 * @param dto
	 * @return
	 */
	public String getFlowElements(ProcDefDto dto) {
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().deploymentId(dto.getDeploymentId()).singleResult();
		if (pd == null) {
			return null;
		}
		BpmnModel model = repositoryService.getBpmnModel(pd.getId());
		Collection<FlowElement> flowElements = null;
		List<FlowElementDto> userTaskElements = new ArrayList<FlowElementDto>();
		if (model != null) {
			flowElements = model.getMainProcess().getFlowElements();
			if (CollectionUtils.isNotEmpty(flowElements)) {
				for (FlowElement e : flowElements) {
					FlowElementDto element = new FlowElementDto();
					element.setId(e.getId());
					element.setName(e.getName());
					if (e instanceof StartEvent) {
						List<SequenceFlow> inFlowList = ((StartEvent) e).getIncomingFlows();
						List<SequenceFlow> outFlowList = ((StartEvent) e).getOutgoingFlows();
						element.setElementType(ElementType.START_EVENT.getValue());
						element.setIncoming(getIncomingList(inFlowList));
						element.setOutgoing(getOutgoingList(outFlowList));
						userTaskElements.add(element);
					}
					if (e instanceof UserTask) {
						List<SequenceFlow> dataInList = ((UserTask) e).getIncomingFlows();
						List<SequenceFlow> dataOutList = ((UserTask) e).getOutgoingFlows();
						element.setElementType(ElementType.USER_TASK.getValue());
						element.setIncoming(getIncomingList(dataInList));
						element.setOutgoing(getOutgoingList(dataOutList));
						List<TaskUserDto> taskList = new ArrayList<TaskUserDto>();
						TaskUserDto taskUser = new TaskUserDto();
						taskUser.setAssignee(((UserTask) e).getAssignee());
						taskUser.setAssigneeUsers(((UserTask) e).getCandidateUsers());
						if (((UserTask) e).getLoopCharacteristics() != null) {
							taskUser.setCompletionCondition(((UserTask) e).getLoopCharacteristics().getCompletionCondition());
							taskUser.setElementVariable(((UserTask) e).getLoopCharacteristics().getElementVariable());
							taskUser.setInputDataItem(((UserTask) e).getLoopCharacteristics().getInputDataItem());
							taskUser.setSequential(((UserTask) e).getLoopCharacteristics().isSequential());
						}
						taskList.add(taskUser);
						element.setTaskUserList(taskList);

						userTaskElements.add(element);
					}
					if (e instanceof EndEvent) {
						List<SequenceFlow> inFlowList = ((EndEvent) e).getIncomingFlows();
						List<SequenceFlow> outFlowList = ((EndEvent) e).getOutgoingFlows();
						element.setElementType(ElementType.END_EVENT.getValue());
						element.setIncoming(getIncomingList(inFlowList));
						element.setOutgoing(getOutgoingList(outFlowList));
						userTaskElements.add(element);
					}
					if (e instanceof ExclusiveGateway) {
						List<SequenceFlow> inFlowList = ((ExclusiveGateway) e).getIncomingFlows();
						List<SequenceFlow> outFlowList = ((ExclusiveGateway) e).getOutgoingFlows();
						element.setElementType(ElementType.EXCLUSIVE.getValue());
						element.setIncoming(getIncomingList(inFlowList));
						element.setOutgoing(getOutgoingList(outFlowList));
						userTaskElements.add(element);
					}
					if (e instanceof ParallelGateway) {
						List<SequenceFlow> inFlowList = ((ParallelGateway) e).getIncomingFlows();
						List<SequenceFlow> outFlowList = ((ParallelGateway) e).getOutgoingFlows();
						element.setElementType(ElementType.PARALLEL.getValue());
						element.setIncoming(getIncomingList(inFlowList));
						element.setOutgoing(getOutgoingList(outFlowList));
						userTaskElements.add(element);
					}
				}
			}
		}

		return JSONArray.fromObject(userTaskElements).toString();
	}

	/**
	 * 查询审批人，抄送人
	 * 
	 * @param procDefKey
	 * @param companyId
	 * @return
	 */
	public ProcBranchResponse getProcBranchUsers(String procDefKey, long companyId) {
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionKey(procDefKey)
				.processDefinitionTenantId(String.valueOf(companyId)).latestVersion().singleResult();
		if (pd == null) {
			return null;
		}
		ProcBranchResponse response = new ProcBranchResponse();
		response.setCompanyId(companyId);
		response.setProcDefkey(procDefKey);
		List<CompUserDto> assigneeList = new ArrayList<CompUserDto>();
		List<CompUserDto> ccList = new ArrayList<CompUserDto>();
		BpmnModel model = repositoryService.getBpmnModel(pd.getId());
		Collection<FlowElement> flowElements = null;
		if (model != null) {
			flowElements = model.getMainProcess().getFlowElements();
			if (CollectionUtils.isNotEmpty(flowElements)) {
				for (FlowElement e : flowElements) {
					if (e instanceof UserTask) {
						if (StringUtils.isNotBlank(((UserTask) e).getAssignee())) {// 审批人列表
							CompUserDto userDto = compUserApi.queryCompUserInfoById(Long.valueOf(((UserTask) e).getAssignee()), true);
							if (userDto != null) {
								assigneeList.add(userDto);
							}
						}
					}
				}
				List<BranchPersonDto> auditorList = DTOConvert.convert2DTO(assigneeList, BranchPersonDto.class);
				response.setAuditorList(auditorList);
				List<Long> ccPersonIds = actBranchCcMapper.selectCcPersonIdsByProcDefKey(procDefKey, companyId);
				if (CollectionUtils.isNotEmpty(ccPersonIds)) {// 抄送人列表
					for (Long ccId : ccPersonIds) {
						CompUserDto userDto = compUserApi.queryCompUserInfoById(ccId, true);
						if (userDto != null) {
							ccList.add(userDto);
						}
					}
					List<BranchPersonDto> ccPersonList = DTOConvert.convert2DTO(ccList, BranchPersonDto.class);
					response.setCcPersonList(ccPersonList);
				}
				return response;
			}
		}
		return null;
	}

	/**
	 * 获取某个流程实例的详细信息
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public ProcessInstance getProcessInstanceById(String processInstanceId, String companyId) {
		return runtimeService.createProcessInstanceQuery().processInstanceTenantId(companyId).processInstanceId(processInstanceId).singleResult();
	}

	/**
	 * 查询历史流程实例
	 * 
	 * @param processInstanceId
	 * @param companyId
	 * @return
	 */
	public HistoricProcessInstance getHisProcessInstanceById(String processInstanceId, String companyId) {
		return historyService.createHistoricProcessInstanceQuery().processInstanceTenantId(companyId).processInstanceId(processInstanceId)
				.singleResult();
	}

	// /**
	// * 取下一个节点的审批人
	// *
	// * @param taskId
	// * @return
	// */
	// private List<UserTask> getNextTaskUserByTaskId(String taskId) {
	// List<UserTask> list = new ArrayList<UserTask>();
	// List<FlowElement> fList = getNextNode(taskId);
	// for (FlowElement u : fList) {
	// UserTask str = (org.activiti.bpmn.model.UserTask) u;
	// list.add(str);
	// }
	// return list;
	// }
	//
	// /**
	// * 获取流程的下一个节点 且要经过规则引擎判断后的节点
	// *
	// * @param taskId
	// * @return
	// */
	// private List<FlowElement> getNextNode(String taskId) {
	// Task task = null;
	// task = taskService.createTaskQuery().taskId(taskId).singleResult();
	// if (task == null) {
	// return null;
	// }
	// List<FlowElement> list = new ArrayList<FlowElement>();
	// ProcessInstance processInstance =
	// runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
	// // 当前活动节点
	// String activitiId = processInstance.getActivityId();
	// System.out.println("当前节点是【" + activitiId + "】");
	// // bpmnModel 遍历节点需要它
	// BpmnModel bpmnModel =
	// repositoryService.getBpmnModel(task.getProcessDefinitionId());
	// org.activiti.bpmn.model.Process process =
	// bpmnModel.getProcessById(task.getProcessDefinitionId());
	//
	// // 循环多个物理流程
	// if (process != null) {
	// // 返回该流程的所有任务，事件
	// Collection<FlowElement> cColl = process.getFlowElements();
	// // 遍历节点
	// for (FlowElement f : cColl) {
	// // 如果改节点是当前节点 者 输出该节点的下一个节点
	// if (f.getId().equals(activitiId)) {
	// List<SequenceFlow> sequenceFlowList = new ArrayList<SequenceFlow>();
	// // 通过反射来判断是哪种类型
	// if (f instanceof org.activiti.bpmn.model.UserTask) {
	// sequenceFlowList = ((org.activiti.bpmn.model.UserTask)
	// f).getOutgoingFlows();
	// for (SequenceFlow sf : sequenceFlowList) {
	// String targetRef = sf.getTargetRef();
	// FlowElement ref = process.getFlowElement(targetRef);
	// list.add(ref);
	// }
	// }
	// break;
	// }
	// }
	// }
	// return list;
	// }
}
