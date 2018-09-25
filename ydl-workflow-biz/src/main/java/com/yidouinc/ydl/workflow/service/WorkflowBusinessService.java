/**
 * 
 */
package com.yidouinc.ydl.workflow.service;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yidouinc.contract.api.ContractApi;
import com.yidouinc.contract.dto.ContractDto;
import com.yidouinc.mars.common.utils.DTOConvert;
import com.yidouinc.mars.enums.YdlModuleTypeEnum;
import com.yidouinc.ydl.tenant.api.CompUserApi;
import com.yidouinc.ydl.tenant.dto.CompUserDto;
import com.yidouinc.ydl.workflow.api.ActOperatingFormApi;
import com.yidouinc.ydl.workflow.domain.ActBusinessForm;
import com.yidouinc.ydl.workflow.domain.ActCcPerson;
import com.yidouinc.ydl.workflow.domain.ActCcPersonExample;
import com.yidouinc.ydl.workflow.domain.ActOperating;
import com.yidouinc.ydl.workflow.dto.ActBusinessFormDto;
import com.yidouinc.ydl.workflow.dto.ActOperatingDto;
import com.yidouinc.ydl.workflow.dto.ActOperatingFormDto;
import com.yidouinc.ydl.workflow.dto.ProcInstanceDto;
import com.yidouinc.ydl.workflow.dto.ProcessDetailDto;
import com.yidouinc.ydl.workflow.dto.TaskDto;
import com.yidouinc.ydl.workflow.enums.OperatingType;
import com.yidouinc.ydl.workflow.enums.TaskStatus;
import com.yidouinc.ydl.workflow.mapper.ActCcPersonMapper;
import com.yidouinc.ydl.workflow.query.WorkflowQuery;

/**
 * @author angq 流程业务操作
 *
 */
@Service
@Transactional
public class WorkflowBusinessService {

	@Autowired
	private ActCcPersonService actCcPersonService;

	@Autowired
	private ActOperatingService actOperatingService;

	@Autowired
	private ActOperatingFormApi actOperatingFormApi;

	@Autowired
	private ActBusinessFormService actBusinessFormService;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private ContractApi contractApi;

	@Autowired
	private CompUserApi compUserApi;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private ActCcPersonMapper actCcPersonMapper;
	
	/**
	 * 流程启动,保存业务信息
	 * 
	 * @param dto
	 * @return
	 */
	public void saveWorkflowRelateInfo(ProcInstanceDto dto) {
		actOperatingService.saveOperatingForProcessInstance(dto);
		actBusinessFormService.saveBusinessForm(dto);
		actCcPersonService.saveCcPerson(dto);
	}

	/**
	 * 任务审批通过或驳回, 保存业务信息
	 * 
	 * @param dto
	 */
	public void saveTaskCommitRelateInfo(TaskDto dto) {
		ActOperatingFormDto actOperatingForm = actOperatingFormApi.queryOperatingForm(Long.valueOf(dto.getBusinessKey()), dto.getBusinessType(),
				dto.getCompanyId());
		dto.setOperatingFormId(actOperatingForm.getId());
		actOperatingService.saveOperatingForTask(dto);
	}

	/**
	 * 设置我发起的流程实例回填信息
	 * 
	 * @param dto
	 * @return
	 */
	public ProcInstanceDto setProcInstDtoForOperating(ProcInstanceDto dto) {
		String[] keyAndType = dto.getBusinessKey().split("-");
		String businessKey = keyAndType[1];
		String businessType = keyAndType[0];
		dto.setBusinessKey(businessKey);
		dto.setBusinessType(businessType);
		ActOperatingFormDto actOperatingForm = actOperatingFormApi.queryOperatingForm(Long.valueOf(businessKey), businessType, dto.getCompanyId());
		dto.setOperatingFormId(actOperatingForm.getId());
		ActOperating actOperating = actOperatingService.queryLastOperatingByProcInstId(dto.getProcInstId(), actOperatingForm.getId(), dto.getCompanyId());
		dto.setAssignee(String.valueOf(actOperating.getOperatorId()));
		CompUserDto aggieneeDto = compUserApi.queryCompUserInfoById(Long.valueOf(dto.getAssignee()), true);
		if (aggieneeDto != null) {// 审批人
			dto.setAssignName(aggieneeDto.getName());
		}
		CompUserDto startUserDto = compUserApi.queryCompUserInfoById(Long.valueOf(dto.getStartUserId()), true);
		if (startUserDto != null) {// 发起人
			dto.setStartUserName(startUserDto.getName());
		}
		if (actOperating.getType() == (byte) OperatingType.PENDING.getValue()) {// 待审批
			dto.setStatus(TaskStatus.TODO.getValue());
		} else if (actOperating.getType() == (byte) OperatingType.PASS.getValue()) {// 通过
			dto.setStatus(TaskStatus.DONE.getValue());
		} else if (actOperating.getType() == (byte) OperatingType.REJECT.getValue()) {// 驳回
			dto.setStatus(TaskStatus.REJECT.getValue());
		} else if (actOperating.getType() == (byte) OperatingType.CANCLE.getValue()) {// 撤销
			dto.setStatus(TaskStatus.CANCLE.getValue());
		}
		if (businessType.equals(YdlModuleTypeEnum.CONTRACT.getValue())) {
			dto.setBusinessName(contractApi.queryContractNameById(Long.valueOf(businessKey), dto.getCompanyId()));
		}

		return dto;
	}

	/**
	 * 设置我审批的任务回填信息
	 * 
	 * @param dto
	 * @return
	 */
	public ProcInstanceDto setTaskDtoForOperating(ProcInstanceDto dto) {
		HistoricProcessInstance hisProcInst = workflowService.getHisProcessInstanceById(dto.getProcInstId(),
				String.valueOf(dto.getCompanyId()));
		if (hisProcInst != null) {
			dto.setProcDefKey(hisProcInst.getProcessDefinitionKey());
			dto.setStartUserId(Long.valueOf(hisProcInst.getStartUserId()));
			dto.setStartTime(hisProcInst.getStartTime());
			CompUserDto startUserDto = compUserApi.queryCompUserInfoById(Long.valueOf(hisProcInst.getStartUserId()), true);
			if (startUserDto != null) {// 发起人
				dto.setStartUserName(startUserDto.getName());
			}
			dto.setBusinessKey(hisProcInst.getBusinessKey());
		}
		String[] keyAndType = dto.getBusinessKey().split("-");
		String businessKey = keyAndType[1];
		String businessType = keyAndType[0];
		dto.setBusinessKey(businessKey);
		dto.setBusinessType(businessType);
		ActOperatingFormDto actOperatingForm = actOperatingFormApi.queryOperatingForm(Long.valueOf(businessKey), businessType, dto.getCompanyId());
		dto.setOperatingFormId(actOperatingForm.getId());
		ActOperating actOperating = actOperatingService.queryLastOperatingByProcInstId(dto.getProcInstId(), actOperatingForm.getId(),
				dto.getCompanyId());
		CompUserDto aggieneeDto = compUserApi.queryCompUserInfoById(actOperating.getOperatorId(), true);
		if (aggieneeDto != null) {
			dto.setAssignName(aggieneeDto.getName());
		}
		if (actOperating.getType() == (byte) OperatingType.PENDING.getValue()) {// 待审批
			dto.setStatus(TaskStatus.TODO.getValue());
		} else if (actOperating.getType() == (byte) OperatingType.PASS.getValue()) {// 通过
			dto.setStatus(TaskStatus.DONE.getValue());
		} else if (actOperating.getType() == (byte) OperatingType.REJECT.getValue()) {// 驳回
			dto.setStatus(TaskStatus.REJECT.getValue());
		} else if (actOperating.getType() == (byte) OperatingType.CANCLE.getValue()) {// 撤销
			dto.setStatus(TaskStatus.CANCLE.getValue());
		}
		if (businessType.equals(YdlModuleTypeEnum.CONTRACT.getValue())) {
			dto.setBusinessName(contractApi.queryContractNameById(Long.valueOf(businessKey), dto.getCompanyId()));
		}

		return dto;
	}

	/**
	 * 查询审核详情
	 * 
	 * @param procInstId
	 * @param companyId
	 * @return
	 */
	public ProcessDetailDto getProcessDetail(String procInstId,  long companyId) {
		HistoricProcessInstanceQuery hisProcInstQuery = historyService.createHistoricProcessInstanceQuery();
		HistoricProcessInstance hisProcInst = hisProcInstQuery.processInstanceId(procInstId)
				.processInstanceTenantId(String.valueOf(companyId)).singleResult();
		if (hisProcInst != null) {
			ProcessDetailDto dto = new ProcessDetailDto();
			dto.setCompanyId(companyId);
			dto.setProcInstId(hisProcInst.getId());
			dto.setProcDefKey(hisProcInst.getProcessDefinitionKey());
			dto.setStartTime(hisProcInst.getStartTime());
			dto.setEndTime(hisProcInst.getEndTime());
			if(StringUtils.isNotBlank(hisProcInst.getStartUserId())){
				dto.setStartUserId(Long.valueOf(hisProcInst.getStartUserId()));
				CompUserDto startUserDto = compUserApi.queryCompUserInfoById(Long.valueOf(hisProcInst.getStartUserId()), true);
				if (startUserDto != null) {// 发起人
					dto.setStartUserName(startUserDto.getName());
				}
			}
			String[] businesskeyAndType = hisProcInst.getBusinessKey().split("-");
			String businessKey = businesskeyAndType[1];
			String businessType = businesskeyAndType[0];
			dto.setBusinessKey(businessKey);
			dto.setBusinessType(businessType);
			if (businessType.equals(YdlModuleTypeEnum.CONTRACT.getValue())) {
				ContractDto contract = contractApi.queryContractById(Long.valueOf(businessKey));
				if(contract != null){
					dto.setBusinessManagerId(contract.getManagerId());
				}
			}
			ActOperatingFormDto operatingForm = actOperatingFormApi.queryOperatingForm(Long.valueOf(businessKey), businessType, companyId);
			if (operatingForm != null) {
				dto.setCode(operatingForm.getCode());
				dto.setOperatingFormId(operatingForm.getId());
				ActOperating actOperating = actOperatingService.queryLastOperatingByProcInstId(procInstId, operatingForm.getId(), companyId);
				if (actOperating != null) {
					if (actOperating.getType() == (byte) OperatingType.PENDING.getValue()) {// 审批中
						dto.setStatus(TaskStatus.TODO.getValue());
					} else if (actOperating.getType() == (byte) OperatingType.PASS.getValue()) {// 通过
						dto.setStatus(TaskStatus.DONE.getValue());
					} else if (actOperating.getType() == (byte) OperatingType.REJECT.getValue()) {// 驳回
						dto.setStatus(TaskStatus.REJECT.getValue());
					} else if (actOperating.getType() == (byte) OperatingType.CANCLE.getValue()) {// 撤销
						dto.setStatus(TaskStatus.CANCLE.getValue());
					}
				}
				if (businessType.equals(YdlModuleTypeEnum.CONTRACT.getValue())) {
					dto.setBusinessName(contractApi.queryContractNameById(Long.valueOf(businessKey), dto.getCompanyId()));
				}
				ActBusinessForm businessForm = actBusinessFormService.queryBusinessForm(procInstId, operatingForm.getId(), companyId);
				if (businessForm != null) {
					ActBusinessFormDto businessFormDto = DTOConvert.convert2DTO(businessForm, ActBusinessFormDto.class);
					dto.setBusinessFormDto(businessFormDto);
				}
				List<ActOperating> operatingList = actOperatingService.queryOperatingList(operatingForm.getId(), companyId);
				if(CollectionUtils.isNotEmpty(operatingList)){
					List<ActOperatingDto> operatingDtoList = DTOConvert.convert2DTO(operatingList, ActOperatingDto.class);
					for (ActOperatingDto actOperatingDto : operatingDtoList) {
						CompUserDto operatorDto = compUserApi.queryCompUserInfoById(actOperatingDto.getOperatorId(), true);
						actOperatingDto.setOperatorName(operatorDto.getName());
						actOperatingDto.setAvatar(operatorDto.getAvatar());
						if(actOperatingDto.getType() != null){
							actOperatingDto.setTypeDesc(OperatingType.getDesc(actOperatingDto.getType()));
						}
					}
					dto.setOperatingList(operatingDtoList);
				}
			}
			return dto;
		}
		return null;
	}

	/**
	 * 回填抄送人的审批信息
	 * 
	 * @param actCcPerson
	 * @return
	 */
	private ProcInstanceDto setProcInstDtoForCc(ActCcPerson actCcPerson) {
		ProcInstanceDto dto = new ProcInstanceDto();
		dto.setBusinessKey(String.valueOf(actCcPerson.getModuleId()));
		dto.setBusinessType(actCcPerson.getModuleType());
		dto.setCompanyId(actCcPerson.getCompanyId());
		ActOperating actOperating = actOperatingService.queryLastOperatingByProcInstId(actCcPerson.getProcInstId(), actCcPerson.getOperatingFormId(),
				actCcPerson.getCompanyId());
		dto.setAssignee(String.valueOf(actOperating.getOperatorId()));
		dto.setOperatingFormId(actOperating.getOperatingFormId());
		dto.setOperatingType(actOperating.getType());
		CompUserDto aggieneeDto = compUserApi.queryCompUserInfoById(Long.valueOf(dto.getAssignee()), true);
		if (aggieneeDto != null) {// 审批人
			dto.setAssignName(aggieneeDto.getName());
		}
		HistoricProcessInstance hisPorcInst = workflowService.getHisProcessInstanceById(actCcPerson.getProcInstId(),
				String.valueOf(actCcPerson.getCompanyId()));
		if (hisPorcInst != null) {
			dto.setProcInstId(actCcPerson.getProcInstId());
			dto.setStartTime(hisPorcInst.getStartTime());
			dto.setStartUserId(Long.valueOf(hisPorcInst.getStartUserId()));
			dto.setProcDefKey(hisPorcInst.getProcessDefinitionKey());
			CompUserDto startUserDto = compUserApi.queryCompUserInfoById(Long.valueOf(dto.getStartUserId()), true);
			if (startUserDto != null) {// 发起人
				dto.setStartUserName(startUserDto.getName());
			}
		}
		if (actOperating.getType() == (byte) OperatingType.PENDING.getValue()) {// 待审批
			dto.setStatus(TaskStatus.TODO.getValue());
		} else if (actOperating.getType() == (byte) OperatingType.PASS.getValue()) {// 通过
			dto.setStatus(TaskStatus.DONE.getValue());
		} else if (actOperating.getType() == (byte) OperatingType.REJECT.getValue()) {// 驳回
			dto.setStatus(TaskStatus.REJECT.getValue());
		} else if (actOperating.getType() == (byte) OperatingType.CANCLE.getValue()) {// 撤销
			dto.setStatus(TaskStatus.CANCLE.getValue());
		}
		if (actCcPerson.getModuleType().equals(YdlModuleTypeEnum.CONTRACT.getValue())) {
			dto.setBusinessName(contractApi.queryContractNameById(actCcPerson.getModuleId(), actCcPerson.getCompanyId()));
		}

		return dto;
	}
	
	/**
	 * 查询抄送人的审批列表
	 * 
	 * @param userId
	 * @param companyId
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PageInfo<ProcInstanceDto> queryCcProcInstListByUserId(WorkflowQuery query) {
		PageHelper.startPage(query.getCurrentPage(), query.getPageSize(), true, false, true);
		ActCcPersonExample example = new ActCcPersonExample();
		ActCcPersonExample.Criteria criteria = example.createCriteria();
		criteria.andCompanyIdEqualTo(query.getCompanyId());
		criteria.andPersonIdEqualTo(query.getUserId());
		List<ActCcPerson> personList = actCcPersonMapper.selectByExample(example);
		List<ProcInstanceDto> dtoList = new ArrayList<ProcInstanceDto>();
		if (CollectionUtils.isNotEmpty(personList)) {
			for (ActCcPerson actCcPerson : personList) {
				ProcInstanceDto dto = setProcInstDtoForCc(actCcPerson);
				dtoList.add(dto);
			}
		}
		PageInfo pageInfo = new PageInfo(personList);
		pageInfo.setList(dtoList);
		return pageInfo;

	}
}
