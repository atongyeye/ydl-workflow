/**
 * 
 */
package com.yidouinc.ydl.workflow.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yidouinc.contract.api.ContractApi;
import com.yidouinc.contract.enums.ContractAuditStatus;
import com.yidouinc.mars.enums.YdlModuleTypeEnum;
import com.yidouinc.mars.response.OperResult;
import com.yidouinc.message.api.MessageApi;
import com.yidouinc.message.dto.InformationDto;
import com.yidouinc.message.enums.ChannelType;
import com.yidouinc.message.enums.InfoType;
import com.yidouinc.ydl.tenant.api.CompUserApi;
import com.yidouinc.ydl.tenant.dto.CompUserDto;
import com.yidouinc.ydl.workflow.api.ActOperatingFormApi;
import com.yidouinc.ydl.workflow.domain.ActOperating;
import com.yidouinc.ydl.workflow.domain.ActOperatingExample;
import com.yidouinc.ydl.workflow.domain.ActOperatingForm;
import com.yidouinc.ydl.workflow.dto.ActOperatingFormDto;
import com.yidouinc.ydl.workflow.dto.ProcInstanceDto;
import com.yidouinc.ydl.workflow.dto.TaskDto;
import com.yidouinc.ydl.workflow.enums.OperatingType;
import com.yidouinc.ydl.workflow.mapper.ActOperatingFormMapper;
import com.yidouinc.ydl.workflow.mapper.ActOperatingMapper;

/**
 * @author angq 操作信息
 *
 */
@Service
@Transactional
public class ActOperatingService {

	private final static Logger logger = LoggerFactory.getLogger(ActOperatingService.class);
	
	@Autowired
	private TaskService taskService;

	@Autowired
	private ActOperatingFormMapper actOperatingFormMapper;

	@Autowired
	private ActOperatingFormApi actOperatingFormApi;
	
	@Autowired
	private ActOperatingMapper actOperatingMapper;

	@Autowired
	private ContractApi contractApi;

	@Autowired
	private ActCcPersonService actCcPersonService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private MessageApi messageApi;

	@Autowired
	private CompUserApi compUserApi;

	/**
	 * 发起,重新发起,撤销,评论流程操作,保存操作信息
	 * 
	 * @param dto
	 */
	public OperResult saveOperatingForProcessInstance(ProcInstanceDto dto) {
		Date now = new Date();
		ActOperating operating = new ActOperating();
		operating.setCompanyId(dto.getCompanyId());
		operating.setProcInstId(dto.getProcInstId());
		operating.setContent(dto.getContent());
		operating.setOperatingFormId(dto.getOperatingFormId());
		operating.setOperatorId(dto.getOperatorId());
		operating.setOperatingTime(now);
		operating.setType(dto.getOperatingType());
		actOperatingMapper.insertSelective(operating);
		if (dto.getOperatingType() == (byte) OperatingType.START.getValue() || dto.getOperatingType() == (byte) OperatingType.RE_START.getValue()) {
			// 根据流程实例 查询待审批任
			List<Task> taskList = taskService.createTaskQuery().processInstanceId(dto.getProcInstId())
					.taskTenantId(String.valueOf(dto.getCompanyId())).list();
			if (CollectionUtils.isNotEmpty(taskList)) {
				List<Long> assigneeList = new ArrayList<Long>();// 钉钉通知人员列表
				for (Task task : taskList) {
					if (StringUtils.isNotBlank(task.getAssignee())) {
						Long assignee = Long.valueOf(task.getAssignee());
						ActOperating waitOperating = new ActOperating();
						waitOperating.setCompanyId(dto.getCompanyId());
						waitOperating.setProcInstId(dto.getProcInstId());
						waitOperating.setOperatingFormId(dto.getOperatingFormId());
						waitOperating.setOperatorId(assignee);
						waitOperating.setType((byte) OperatingType.PENDING.getValue());
						actOperatingMapper.insertSelective(waitOperating);
						assigneeList.add(assignee);
					}
				}
				// 系统给当前审批人发送《钉钉》工作通知。
				if (CollectionUtils.isNotEmpty(assigneeList)) {
					List<Long> newList = new ArrayList<Long>(new HashSet<Long>(assigneeList));
					sendMessage(true, dto.getOperatorId(), dto.getOperatingType(), newList, dto.getBusinessKey(), dto.getBusinessType(),
							dto.getProcInstId(), dto.getProcDefKey(), dto.getCompanyId());
				}
				if (dto.getBusinessType().equals(YdlModuleTypeEnum.CONTRACT.getValue())) {
					contractApi.updateContractAuditStatus(Long.valueOf(dto.getBusinessKey()), ContractAuditStatus.AUDITING.getValue(),
							dto.getOperatorId());
				}
			}
		} else if (dto.getOperatingType() == (byte) OperatingType.COMMENT.getValue()) {
			List<Long> operatorIds = queryOperatorIds(dto.getProcInstId(), dto.getOperatingFormId(), dto.getCompanyId());
			if (CollectionUtils.isNotEmpty(operatorIds)) {
				List<Long> newList = new ArrayList<Long>(new HashSet<Long>(operatorIds));
				sendMessage(false, dto.getOperatorId(), dto.getOperatingType(), newList, dto.getBusinessKey(), dto.getBusinessType(),
						dto.getProcInstId(), dto.getProcDefKey(), dto.getCompanyId());
			}
		}
		return OperResult.getSuccessResult();
	}

	/**
	 * 通过,驳回操作,保存操作信息
	 * 
	 * @param dto
	 */
	public OperResult saveOperatingForTask(TaskDto dto) {

		ActOperatingExample example = new ActOperatingExample();
		ActOperatingExample.Criteria criteria = example.createCriteria();
		criteria.andCompanyIdEqualTo(dto.getCompanyId());
		criteria.andProcInstIdEqualTo(dto.getProcInstId());
		criteria.andOperatingTimeIsNull();
		List<ActOperating> list = actOperatingMapper.selectByExample(example);
		if (CollectionUtils.isNotEmpty(list)) {
			for (ActOperating actOperating : list) {
				actOperating.setOperatingTime(new Date());
				actOperating.setType(dto.getOperatingType());
				actOperating.setOperatorId(dto.getOperatorId());
				actOperating.setContent(dto.getContent());
				actOperatingMapper.updateByPrimaryKeySelective(actOperating);
			}
		}
		if (dto.getOperatingType() == (byte) OperatingType.PASS.getValue()) {// 通过,设置下一节点审批中
			// 先执行任务审批,再调此接口
			List<Task> taskList = taskService.createTaskQuery().processInstanceId(dto.getProcInstId())
					.taskTenantId(String.valueOf(dto.getCompanyId())).list();
			List<Long> assigneeList = new ArrayList<Long>();// 钉钉通知人员列表
			if (CollectionUtils.isNotEmpty(taskList)) {
				for (Task task : taskList) {
					if (StringUtils.isNotBlank(task.getAssignee())) {
						Long assignee = Long.valueOf(task.getAssignee());
						ActOperating waitOperating = new ActOperating();
						waitOperating.setCompanyId(dto.getCompanyId());
						waitOperating.setProcInstId(dto.getProcInstId());
						waitOperating.setOperatingFormId(dto.getOperatingFormId());
						waitOperating.setOperatorId(assignee);
						waitOperating.setType((byte) OperatingType.PENDING.getValue());
						actOperatingMapper.insertSelective(waitOperating);
						assigneeList.add(assignee);
					}
				}
				// 通知对象：下一审批人
				if (CollectionUtils.isNotEmpty(assigneeList)) {
					List<Long> newList = new ArrayList<Long>(new HashSet<Long>(assigneeList));
					sendMessage(true, dto.getOperatorId(), dto.getOperatingType(), newList, dto.getBusinessKey(), dto.getBusinessType(),
							dto.getProcInstId(), dto.getProcDefKey(), dto.getCompanyId());
				}
			} else {
				// 流程结束，通知对象：申请人、所有审批人、所有抄送人。
				List<Long> operatorIds = queryOperatorIds(dto.getProcInstId(), dto.getOperatingFormId(), dto.getCompanyId());
				List<Long> ccPersonIds = actCcPersonService.queryCcPersonIds(dto.getBusinessType(), Long.valueOf(dto.getBusinessKey()),
						dto.getProcInstId(), dto.getCompanyId());
				assigneeList.addAll(operatorIds);
				assigneeList.addAll(ccPersonIds);
				if (CollectionUtils.isNotEmpty(assigneeList)) {
					List<Long> newList = new ArrayList<Long>(new HashSet<Long>(assigneeList));
					sendMessage(false, dto.getOperatorId(), dto.getOperatingType(), newList, dto.getBusinessKey(), dto.getBusinessType(),
							dto.getProcInstId(), dto.getProcDefKey(), dto.getCompanyId());
				}
				if (dto.getBusinessType().equals(YdlModuleTypeEnum.CONTRACT.getValue())) {// 更新合同状态
					contractApi.updateContractAuditStatus(Long.valueOf(dto.getBusinessKey()), ContractAuditStatus.PASS.getValue(),
							dto.getOperatorId());
				}
			}
		} else if (dto.getOperatingType() == (byte) OperatingType.REJECT.getValue()) {
			// 通知对象：申请人、已经参与过审批或评论操作的人员。
			List<Long> operatorIds = queryOperatorIds(dto.getProcInstId(), dto.getOperatingFormId(), dto.getCompanyId());
			if (CollectionUtils.isNotEmpty(operatorIds)) {
				List<Long> newList = new ArrayList<Long>(new HashSet<Long>(operatorIds));
				sendMessage(false, dto.getOperatorId(), dto.getOperatingType(), newList, dto.getBusinessKey(), dto.getBusinessType(),
						dto.getProcInstId(), dto.getProcDefKey(), dto.getCompanyId());
			}
			if (dto.getBusinessType().equals(YdlModuleTypeEnum.CONTRACT.getValue())) {// 更新合同状态
				contractApi.updateContractAuditStatus(Long.valueOf(dto.getBusinessKey()), ContractAuditStatus.REJECT.getValue(), dto.getOperatorId());
			}
		} else if (dto.getOperatingType() == (byte) OperatingType.CANCLE.getValue()) {
			List<Long> operatorIds = queryOperatorIds(dto.getProcInstId(), dto.getOperatingFormId(), dto.getCompanyId());
			if (CollectionUtils.isNotEmpty(operatorIds)) {
				// TODO 如果撤销操作人是发起人，过滤掉发起人?
				List<Long> newList = new ArrayList<Long>(new HashSet<Long>(operatorIds));
				sendMessage(false, dto.getOperatorId(), dto.getOperatingType(), newList, dto.getBusinessKey(), dto.getBusinessType(),
						dto.getProcInstId(), dto.getProcDefKey(), dto.getCompanyId());
			}
			if (dto.getBusinessType().equals(YdlModuleTypeEnum.CONTRACT.getValue())) {// 更新合同状态
																						// 撤销
				contractApi.updateContractAuditStatus(Long.valueOf(dto.getBusinessKey()), ContractAuditStatus.CANCLE.getValue(), dto.getOperatorId());
			}
		}
		return OperResult.getSuccessResult();
	}

	/**
	 * 根据审批单id 查询操作信息列表
	 * 
	 * @param operatingFormId
	 * @param companyId
	 * @return
	 */
	public List<ActOperating> queryOperatingList(Long operatingFormId, Long companyId) {
		List<ActOperating> listAll = new ArrayList<ActOperating>();
		ActOperatingExample exampleApproved = new ActOperatingExample();
		ActOperatingExample.Criteria criteriaApproved = exampleApproved.createCriteria();
		criteriaApproved.andCompanyIdEqualTo(companyId);
		criteriaApproved.andOperatingFormIdEqualTo(operatingFormId);
		criteriaApproved.andOperatingTimeIsNull();
		criteriaApproved.andTypeEqualTo((byte) OperatingType.PENDING.getValue());
		List<ActOperating> list = actOperatingMapper.selectByExample(exampleApproved);
		if(CollectionUtils.isNotEmpty(list)){
			listAll.addAll(list);
		}
		ActOperatingExample example = new ActOperatingExample();
		ActOperatingExample.Criteria criteria = example.createCriteria();
		criteria.andCompanyIdEqualTo(companyId);
		criteria.andOperatingFormIdEqualTo(operatingFormId);
		criteria.andOperatingTimeIsNotNull();
		example.setOrderByClause("operating_time desc");
		List<ActOperating> listDone = actOperatingMapper.selectByExample(example);
		if(CollectionUtils.isNotEmpty(listDone)){
			listAll.addAll(listDone);
		}
		return listAll;
	}

	/**
	 * 根据模块id查询流程实例id
	 * 
	 * @param operatingFormId
	 * @param companyId
	 * @return
	 */
	public String queryProcInstIdByModuleId(Long moduleId,String moduleType,  Long companyId) {
		ActOperatingFormDto operatingForm = actOperatingFormApi.queryOperatingForm(moduleId, moduleType, companyId);
		if(operatingForm != null){
			ActOperatingExample exampleApproved = new ActOperatingExample();
			ActOperatingExample.Criteria criteriaApproved = exampleApproved.createCriteria();
			criteriaApproved.andCompanyIdEqualTo(companyId);
			criteriaApproved.andOperatingFormIdEqualTo(operatingForm.getId());
			criteriaApproved.andTypeNotEqualTo((byte) OperatingType.COMMENT.getValue());
			List<ActOperating> list = actOperatingMapper.selectByExample(exampleApproved);
			if(CollectionUtils.isNotEmpty(list)){
				return list.get(0).getProcInstId();
			}
		}
		return null;
	}
	
	/**
	 * 查询最近操作记录
	 * 
	 * @param operatingFormId
	 * @param companyId
	 * @return
	 */
	public ActOperating queryOperatingForApproved(Long operatingFormId, Long companyId) {
		ActOperatingExample exampleApproved = new ActOperatingExample();
		ActOperatingExample.Criteria criteriaApproved = exampleApproved.createCriteria();
		criteriaApproved.andCompanyIdEqualTo(companyId);
		criteriaApproved.andOperatingFormIdEqualTo(operatingFormId);
		criteriaApproved.andOperatingTimeIsNull();
		criteriaApproved.andTypeEqualTo((byte) OperatingType.PENDING.getValue());
		List<ActOperating> list = actOperatingMapper.selectByExample(exampleApproved);
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 查询最近操作记录
	 * 
	 * @param query
	 * @return
	 */
	public ActOperating queryLastOperatingByProcInstId(String procInstId, Long operatingFormId, Long companyId) {
		ActOperating actOperating = actOperatingMapper.selectLastOperatingByProcInstId(procInstId, operatingFormId, companyId);
		return actOperating;
	}

	/**
	 * 查询操作人ids
	 * 
	 * @param procInstId
	 * @param operatingFormId
	 * @param companyId
	 * @return
	 */
	public List<Long> queryOperatorIds(String procInstId, Long operatingFormId, Long companyId) {
		return actOperatingMapper.selectOperatorIdsByProcInstId(procInstId, operatingFormId, companyId);
	}
	
	/**
	 * 判断模块是否已在审批中
	 * 
	 * @param moduleId
	 * @param moduleType
	 * @param companyId
	 * @return
	 */
	public boolean queryApprovedModuleExist(long moduleId, String moduleType, long companyId) {
		ActOperatingForm operatingForm = actOperatingFormMapper.selectOperatingFormByModuleId(moduleId, moduleType, companyId);
		if (operatingForm == null) {// 未发起过审批
			return false;
		} else {
			int count = actOperatingMapper.selectApprovedByOperatingFormId(operatingForm.getId(), companyId);
			if (count > 0) {//已在审批中
				return true;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * 流程操作,推送钉钉消息
	 * 
	 * @param hasNextTask
	 * @param operatorId
	 * @param operatingType
	 * @param noticeUserIds
	 * @param businessKey
	 * @param businessType
	 * @param procInstId
	 * @param procDefKey
	 * @param companyId
	 */
	private void sendMessage(boolean hasNextTask, Long operatorId, byte operatingType, List<Long> noticeUserIds, String businessKey,
			String businessType, String procInstId, String procDefKey, Long companyId) {
		logger.info("通知人选:"+noticeUserIds);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String operatingTime = sdf.format(new Date());
		if (CollectionUtils.isNotEmpty(noticeUserIds)) {
			String businessName = null;// 模块名称
			if (businessType.equals(YdlModuleTypeEnum.CONTRACT.getValue())) {
				businessName = contractApi.queryContractNameById(Long.valueOf(businessKey), companyId);
			}
			for (Long noticeUserId : noticeUserIds) {
				CompUserDto operatorDto = compUserApi.queryCompUserInfoById(operatorId, true);
				if (operatorDto != null) {
					InformationDto informationDto = new InformationDto();
					if (operatingType == (byte) OperatingType.PASS.getValue()) {
						if (businessType.equals(YdlModuleTypeEnum.CONTRACT.getValue())) {
							informationDto.setModuleName("合同审批");
						}
						if (hasNextTask) {
							informationDto.setContent("[" + operatorDto.getName() + "]于[" + operatingTime + "]通过[" + businessName + "]的审批申请");

						} else {
							HistoricProcessInstance hisProcInst = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInstId)
									.processInstanceTenantId(String.valueOf(companyId)).singleResult();
							if (hisProcInst != null) {
								CompUserDto startUserDto = compUserApi.queryCompUserInfoById(Long.valueOf(hisProcInst.getStartUserId()), true);
								if (startUserDto != null) {
									informationDto.setContent("[" + startUserDto.getName() + "]于[" + sdf.format(hisProcInst.getStartTime()) + "]发起的["
											+ businessName + "]的审批通过");
								}
							}
						}
					} else if (operatingType == (byte) OperatingType.REJECT.getValue()) {
						if (businessType.equals(YdlModuleTypeEnum.CONTRACT.getValue())) {
							informationDto.setModuleName("审批驳回");
						}
						HistoricProcessInstance hisProcInst = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInstId)
								.processInstanceTenantId(String.valueOf(companyId)).singleResult();
						if (hisProcInst != null) {
							CompUserDto startUserDto = compUserApi.queryCompUserInfoById(Long.valueOf(hisProcInst.getStartUserId()), true);
							if (startUserDto != null) {
								informationDto.setContent("[" + startUserDto.getName() + "]于[" + sdf.format(hisProcInst.getStartTime()) + "]发起的["
										+ businessName + "]的审批被驳回");
							}
						}
					} else if (operatingType == (byte) OperatingType.COMMENT.getValue()) {
						if (businessType.equals(YdlModuleTypeEnum.CONTRACT.getValue())) {
							informationDto.setModuleName("审批评论");
						}
						if (operatorDto != null) {
							informationDto.setContent("[" + operatorDto.getName() + "]于[" + sdf.format(new Date()) + "]发表对[" + businessName + "]的评论");
						}
					} else if (operatingType == (byte) OperatingType.START.getValue() || operatingType == (byte) OperatingType.RE_START.getValue()) {
						if (businessType.equals(YdlModuleTypeEnum.CONTRACT.getValue())) {
							informationDto.setModuleName("合同审批");
						}
						informationDto.setContent("[" + operatorDto.getName() + "]于[" + operatingTime + "]发起[" + businessName + "]审批申请");
					} else if (operatingType == (byte) OperatingType.CANCLE.getValue()) {
						if (businessType.equals(YdlModuleTypeEnum.CONTRACT.getValue())) {
							informationDto.setModuleName("撤销审批");
						}
						informationDto.setContent("[" + operatorDto.getName() + "]于[" + operatingTime + "]撤销了[" + businessName + "]的审批申请");
					}
					informationDto.setCreatedTime(new Date());
					if (businessType.equals(YdlModuleTypeEnum.CONTRACT.getValue())) {
						informationDto.setModuleType(YdlModuleTypeEnum.CONTRACT_AUDIT.getValue());
					}
					informationDto.setModuleId(Long.valueOf(procInstId));
					informationDto.setCode(procDefKey);
					informationDto.setSenderId(operatorId);
					informationDto.setSenderName(operatorDto.getName());
					informationDto.setReceiverId(noticeUserId);
					informationDto.setCompanyId(companyId);
					informationDto.setTanendId(companyId);
					informationDto.setType(InfoType.NOTIFY.getValue());
					informationDto.setChannel(ChannelType.CHANNEL_DING.getCode());
					messageApi.saveInformation(informationDto);
				}

			}

		}
	}

}
