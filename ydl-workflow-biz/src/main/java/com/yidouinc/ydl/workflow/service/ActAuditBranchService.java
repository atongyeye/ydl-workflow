/**
 * 
 */
package com.yidouinc.ydl.workflow.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yidouinc.mars.common.utils.DTOConvert;
import com.yidouinc.mars.enums.CommonStatus;
import com.yidouinc.mars.enums.YdlModuleTypeEnum;
import com.yidouinc.mars.response.OperResult;
import com.yidouinc.ydl.tenant.api.CompDepartmentApi;
import com.yidouinc.ydl.tenant.api.CompUserApi;
import com.yidouinc.ydl.tenant.dto.CompDepartmentDto;
import com.yidouinc.ydl.tenant.dto.CompUserDto;
import com.yidouinc.ydl.workflow.api.WorkflowApi;
import com.yidouinc.ydl.workflow.api.WorkflowRepositoryApi;
import com.yidouinc.ydl.workflow.domain.ActAuditBranch;
import com.yidouinc.ydl.workflow.domain.ActAuditBranchExample;
import com.yidouinc.ydl.workflow.domain.ActBranchApplicant;
import com.yidouinc.ydl.workflow.domain.ActBranchApplicantExample;
import com.yidouinc.ydl.workflow.domain.ActBranchCc;
import com.yidouinc.ydl.workflow.dto.ActAuditBranchDto;
import com.yidouinc.ydl.workflow.dto.ActBranchApplicantDto;
import com.yidouinc.ydl.workflow.enums.ApplyTypeEnum;
import com.yidouinc.ydl.workflow.enums.PreConditionEnum;
import com.yidouinc.ydl.workflow.mapper.ActAuditBranchMapper;
import com.yidouinc.ydl.workflow.mapper.ActBranchApplicantMapper;
import com.yidouinc.ydl.workflow.mapper.ActBranchCcMapper;
import com.yidouinc.ydl.workflow.query.ActConfigQuery;
import com.yidouinc.ydl.workflow.response.ProcBranchResponse;

/**
 * @author angq 审批分支
 *
 */
@Service
@Transactional
public class ActAuditBranchService {

	@Autowired
	private ActAuditBranchMapper actAuditBranchMapper;

	@Autowired
	private ActBranchApplicantMapper actBranchApplicantMapper;

	@Autowired
	private CompDepartmentApi departmentApi;

	@Autowired
	private WorkflowRepositoryApi workflowRepositoryApi;

	@Autowired
	private ActBranchCcMapper actBranchCcMapper;

	@Autowired
	private WorkflowApi workflowApi;

	@Autowired
	private CompUserApi compUserApi;

	/**
	 * 保存审批分支
	 * 
	 * @param dto
	 * @return
	 */
	public OperResult saveActAuditBranch(ActAuditBranchDto dto) {
		// 校验分支名称是否已存在
		ActAuditBranchExample example = new ActAuditBranchExample();
		ActAuditBranchExample.Criteria criteria = example.createCriteria();
		criteria.andCompanyIdEqualTo(dto.getCompanyId());
		criteria.andNameEqualTo(dto.getName());
		int sameNameCount = actAuditBranchMapper.countByExample(example);
		if (sameNameCount > 0) {
			return OperResult.getErrorResult("分支名称已存在.");
		}
		String procDefKey = "";
		ActAuditBranch actAuditBranch = DTOConvert.convert2DTO(dto, ActAuditBranch.class);
		actAuditBranchMapper.insertSelective(actAuditBranch);
		if (dto.getModuleType().equals(YdlModuleTypeEnum.CONTRACT.getValue())) {// 流程key生成规则
			procDefKey = dto.getModuleType() + "Process" + actAuditBranch.getId() + "-" + dto.getCompanyId();
			dto.getBpmnModleDto().setProcessId(procDefKey);
			dto.getBpmnModleDto().setProcessName(dto.getName());
			actAuditBranch.setProcDefKey(procDefKey);
		}
		actAuditBranchMapper.updateByPrimaryKeySelective(actAuditBranch);
		if (CollectionUtils.isNotEmpty(dto.getUserIds())) {
			for (long userId : dto.getUserIds()) {
				ActBranchApplicant applicant = new ActBranchApplicant();
				applicant.setCompanyId(actAuditBranch.getCompanyId());
				applicant.setCreatorId(actAuditBranch.getCreatorId());
				applicant.setModuleType(actAuditBranch.getModuleType());
				applicant.setProcDefKey(procDefKey);
				applicant.setActBranchId(actAuditBranch.getId());
				applicant.setApplyId(userId);
				applicant.setApplyType((byte) ApplyTypeEnum.APPLY_USER.getValue());
				actBranchApplicantMapper.insertSelective(applicant);
			}
		}
		if (CollectionUtils.isNotEmpty(dto.getDeptIds())) {
			for (long deptId : dto.getDeptIds()) {
				ActBranchApplicant applicant = new ActBranchApplicant();
				applicant.setCompanyId(actAuditBranch.getCompanyId());
				applicant.setCreatorId(actAuditBranch.getCreatorId());
				applicant.setModuleType(actAuditBranch.getModuleType());
				applicant.setProcDefKey(procDefKey);
				applicant.setActBranchId(actAuditBranch.getId());
				applicant.setApplyId(deptId);
				applicant.setApplyType((byte) ApplyTypeEnum.APPLY_DEPT.getValue());
				actBranchApplicantMapper.insertSelective(applicant);
			}
		}
		workflowRepositoryApi.produceBpmnJson(dto.getBpmnModleDto(), dto.getCompanyId());// 生成流程定义
		// 保存分支抄送人信息
		if (CollectionUtils.isNotEmpty(dto.getCcPersonIds())) {
			for (Long ccId : dto.getCcPersonIds()) {
				ActBranchCc branchCc = new ActBranchCc();
				branchCc.setActBranchId(actAuditBranch.getId());
				branchCc.setCompanyId(dto.getCompanyId());
				branchCc.setCreatorId(dto.getCreatorId());
				branchCc.setModuleType(dto.getModuleType());
				branchCc.setPersonId(ccId);
				branchCc.setProcDefKey(procDefKey);
				actBranchCcMapper.insertSelective(branchCc);
			}
		}
		return OperResult.getSuccessResult(actAuditBranch.getId());
	}

	/**
	 * 更新审批分支
	 * 
	 * @param dto
	 * @return
	 */
	public OperResult updateActAuditBranch(ActAuditBranchDto dto) {
		// 校验分支名称是否已存在
		ActAuditBranchExample example = new ActAuditBranchExample();
		ActAuditBranchExample.Criteria criteria = example.createCriteria();
		criteria.andCompanyIdEqualTo(dto.getCompanyId());
		criteria.andNameEqualTo(dto.getName());
		criteria.andIdNotEqualTo(dto.getId());
		int sameNameCount = actAuditBranchMapper.countByExample(example);
		if (sameNameCount > 0) {
			return OperResult.getErrorResult("分支名称已存在.");
		}
		ActAuditBranch actAuditBranch = actAuditBranchMapper.selectByPrimaryKey(dto.getId());
		actAuditBranch.setName(dto.getName());
		actAuditBranch.setPreCondition(dto.getPreCondition());
		actAuditBranch.setStatus(dto.getStatus());
		actAuditBranch.setUpdatorId(dto.getUpdatorId());
		if (StringUtils.isBlank(actAuditBranch.getProcDefKey())) {
			dto.getBpmnModleDto()
					.setProcessId(actAuditBranch.getModuleType() + "Process" + actAuditBranch.getId() + "-" + actAuditBranch.getCompanyId());
			actAuditBranch.setProcDefKey(dto.getBpmnModleDto().getProcessId());
		} else {
			dto.getBpmnModleDto().setProcessId(actAuditBranch.getProcDefKey());
		}
		actAuditBranchMapper.updateByPrimaryKeySelective(actAuditBranch);
		dto.getBpmnModleDto().setProcessName(dto.getName());// 设置流程分支名称
		workflowRepositoryApi.produceBpmnJson(dto.getBpmnModleDto(), dto.getCompanyId());// 生成流程定义
		// 更新分支抄送人信息
		actBranchCcMapper.deleteByBranchId(actAuditBranch.getId(), dto.getCompanyId());
		if (CollectionUtils.isNotEmpty(dto.getCcPersonIds())) {
			for (Long ccId : dto.getCcPersonIds()) {
				ActBranchCc branchCc = new ActBranchCc();
				branchCc.setActBranchId(actAuditBranch.getId());
				branchCc.setCompanyId(dto.getCompanyId());
				branchCc.setCreatorId(dto.getUpdatorId());
				branchCc.setModuleType(actAuditBranch.getModuleType());
				branchCc.setPersonId(ccId);
				branchCc.setProcDefKey(actAuditBranch.getProcDefKey());
				actBranchCcMapper.insertSelective(branchCc);
			}
		}
		// 更新发起人
		ActBranchApplicantExample delExample = new ActBranchApplicantExample();
		ActBranchApplicantExample.Criteria delCriteria = delExample.createCriteria();
		delCriteria.andCompanyIdEqualTo(dto.getCompanyId());
		delCriteria.andActBranchIdEqualTo(dto.getId());
		actBranchApplicantMapper.deleteByExample(delExample);
		if (CollectionUtils.isNotEmpty(dto.getUserIds())) {
			for (long userId : dto.getUserIds()) {
				ActBranchApplicant applicant = new ActBranchApplicant();
				applicant.setCompanyId(actAuditBranch.getCompanyId());
				applicant.setCreatorId(dto.getUpdatorId());
				applicant.setModuleType(actAuditBranch.getModuleType());
				applicant.setProcDefKey(actAuditBranch.getProcDefKey());
				applicant.setActBranchId(actAuditBranch.getId());
				applicant.setApplyId(userId);
				applicant.setApplyType((byte) ApplyTypeEnum.APPLY_USER.getValue());
				actBranchApplicantMapper.insertSelective(applicant);
			}
		}
		if (CollectionUtils.isNotEmpty(dto.getDeptIds())) {
			for (long deptId : dto.getDeptIds()) {
				ActBranchApplicant applicant = new ActBranchApplicant();
				applicant.setCompanyId(actAuditBranch.getCompanyId());
				applicant.setCreatorId(actAuditBranch.getCreatorId());
				applicant.setModuleType(actAuditBranch.getModuleType());
				applicant.setProcDefKey(actAuditBranch.getProcDefKey());
				applicant.setActBranchId(actAuditBranch.getId());
				applicant.setApplyId(deptId);
				applicant.setApplyType((byte) ApplyTypeEnum.APPLY_DEPT.getValue());
				actBranchApplicantMapper.insertSelective(applicant);
			}
		}
		return OperResult.getSuccessResult(actAuditBranch.getId());
	}

	/**
	 * 更新审批分支名称
	 * 
	 * @param dto
	 * @return
	 */
	public OperResult updateActAuditBranchName(ActAuditBranchDto dto) {
		// 校验分支名称是否已存在
		ActAuditBranchExample example = new ActAuditBranchExample();
		ActAuditBranchExample.Criteria criteria = example.createCriteria();
		criteria.andCompanyIdEqualTo(dto.getCompanyId());
		criteria.andNameEqualTo(dto.getName());
		criteria.andIdNotEqualTo(dto.getId());
		int sameNameCount = actAuditBranchMapper.countByExample(example);
		if (sameNameCount > 0) {
			return OperResult.getErrorResult("分支名称已存在.");
		}
		ActAuditBranch actAuditBranch = actAuditBranchMapper.selectByPrimaryKey(dto.getId());
		actAuditBranch.setName(dto.getName());
		actAuditBranch.setUpdatorId(dto.getUpdatorId());
		actAuditBranchMapper.updateByPrimaryKeySelective(actAuditBranch);
		return OperResult.getSuccessResult(actAuditBranch.getId());
	}

	/**
	 * 更新审批分支前置条件
	 * 
	 * @param dto
	 * @return
	 */
	public OperResult updateActAuditBranchCondition(ActAuditBranchDto dto) {
		ActAuditBranch actAuditBranch = actAuditBranchMapper.selectByPrimaryKey(dto.getId());
		actAuditBranch.setUpdatorId(dto.getUpdatorId());
		if (dto.getPreCondition() == (byte) PreConditionEnum.NO_CONDITION.getValue()) {
			actAuditBranch.setPreCondition(dto.getPreCondition());
			actAuditBranchMapper.updateByPrimaryKey(actAuditBranch);
		} else if (dto.getPreCondition() == (byte) PreConditionEnum.HAS_CONDITION.getValue()) {
			actAuditBranch.setPreCondition(dto.getPreCondition());
			actAuditBranchMapper.updateByPrimaryKeySelective(actAuditBranch);
			// 更新发起人
			ActBranchApplicantExample delExample = new ActBranchApplicantExample();
			ActBranchApplicantExample.Criteria delCriteria = delExample.createCriteria();
			delCriteria.andCompanyIdEqualTo(dto.getCompanyId());
			delCriteria.andActBranchIdEqualTo(dto.getId());
			actBranchApplicantMapper.deleteByExample(delExample);
			if (CollectionUtils.isNotEmpty(dto.getUserIds())) {
				for (long userId : dto.getUserIds()) {
					ActBranchApplicant applicant = new ActBranchApplicant();
					applicant.setCompanyId(actAuditBranch.getCompanyId());
					applicant.setCreatorId(dto.getUpdatorId());
					applicant.setModuleType(actAuditBranch.getModuleType());
					applicant.setProcDefKey(actAuditBranch.getProcDefKey());
					applicant.setActBranchId(actAuditBranch.getId());
					applicant.setApplyId(userId);
					applicant.setApplyType((byte) ApplyTypeEnum.APPLY_USER.getValue());
					actBranchApplicantMapper.insertSelective(applicant);
				}
			}
			if (CollectionUtils.isNotEmpty(dto.getDeptIds())) {
				for (long deptId : dto.getDeptIds()) {
					ActBranchApplicant applicant = new ActBranchApplicant();
					applicant.setCompanyId(actAuditBranch.getCompanyId());
					applicant.setCreatorId(actAuditBranch.getCreatorId());
					applicant.setModuleType(actAuditBranch.getModuleType());
					applicant.setProcDefKey(actAuditBranch.getProcDefKey());
					applicant.setActBranchId(actAuditBranch.getId());
					applicant.setApplyId(deptId);
					applicant.setApplyType((byte) ApplyTypeEnum.APPLY_DEPT.getValue());
					actBranchApplicantMapper.insertSelective(applicant);
				}
			}
		}
		return OperResult.getSuccessResult(actAuditBranch.getId());
	}

	/**
	 * 更新审批分支审批人
	 * 
	 * @param dto
	 * @return
	 */
	public OperResult updateActAuditBranchAuditor(ActAuditBranchDto dto) {
		ActAuditBranch actAuditBranch = actAuditBranchMapper.selectByPrimaryKey(dto.getId());
		actAuditBranch.setUpdatorId(dto.getUpdatorId());
		if (StringUtils.isBlank(actAuditBranch.getProcDefKey())) {
			dto.getBpmnModleDto()
					.setProcessId(actAuditBranch.getModuleType() + "Process" + actAuditBranch.getId() + "-" + actAuditBranch.getCompanyId());
			actAuditBranch.setProcDefKey(dto.getBpmnModleDto().getProcessId());
		} else {
			dto.getBpmnModleDto().setProcessId(actAuditBranch.getProcDefKey());
		}
		actAuditBranchMapper.updateByPrimaryKeySelective(actAuditBranch);
		dto.getBpmnModleDto().setProcessName(actAuditBranch.getName());// 设置流程分支名称
		workflowRepositoryApi.produceBpmnJson(dto.getBpmnModleDto(), dto.getCompanyId());// 生成流程定义
		return OperResult.getSuccessResult(actAuditBranch.getId());
	}

	/**
	 * 更新审批分支抄送人
	 * 
	 * @param dto
	 * @return
	 */
	public OperResult updateActAuditBranchCc(ActAuditBranchDto dto) {
		ActAuditBranch actAuditBranch = actAuditBranchMapper.selectByPrimaryKey(dto.getId());
		actAuditBranch.setUpdatorId(dto.getUpdatorId());
		// 更新分支抄送人信息
		actBranchCcMapper.deleteByBranchId(actAuditBranch.getId(), dto.getCompanyId());
		if (CollectionUtils.isNotEmpty(dto.getCcPersonIds())) {
			for (Long ccId : dto.getCcPersonIds()) {
				ActBranchCc branchCc = new ActBranchCc();
				branchCc.setActBranchId(actAuditBranch.getId());
				branchCc.setCompanyId(dto.getCompanyId());
				branchCc.setCreatorId(dto.getUpdatorId());
				branchCc.setModuleType(actAuditBranch.getModuleType());
				branchCc.setPersonId(ccId);
				branchCc.setProcDefKey(actAuditBranch.getProcDefKey());
				actBranchCcMapper.insertSelective(branchCc);
			}
		}
		actAuditBranchMapper.updateByPrimaryKeySelective(actAuditBranch);
		return OperResult.getSuccessResult(actAuditBranch.getId());
	}

	/**
	 * 查询审批分支列表
	 * 
	 * @param dto
	 * @return
	 */
	public List<ActAuditBranchDto> queryAuditBranchList(ActConfigQuery query) {
		ActAuditBranchExample example = new ActAuditBranchExample();
		ActAuditBranchExample.Criteria criteria = example.createCriteria();
		criteria.andCompanyIdEqualTo(query.getCompanyId());
		if (StringUtils.isNotBlank(query.getModuleType())) {
			criteria.andModuleTypeEqualTo(query.getModuleType());
		}
		criteria.andActConfigIdEqualTo(query.getAuditConfigId());
		criteria.andStatusEqualTo((byte) CommonStatus.NOR.getValue());// 启动
		List<ActAuditBranch> list = actAuditBranchMapper.selectByExample(example);
		List<ActAuditBranchDto> dtoList = DTOConvert.convert2DTO(list, ActAuditBranchDto.class);
		if (CollectionUtils.isNotEmpty(dtoList)) {
			for (ActAuditBranchDto actAuditBranchDto : dtoList) {
				if (actAuditBranchDto.getPreCondition() == (byte) PreConditionEnum.HAS_CONDITION.getValue()) {
					ActBranchApplicantExample applicantExample = new ActBranchApplicantExample();
					ActBranchApplicantExample.Criteria applicantCriteria = applicantExample.createCriteria();
					applicantCriteria.andCompanyIdEqualTo(query.getCompanyId());
					applicantCriteria.andActBranchIdEqualTo(actAuditBranchDto.getId());
					List<ActBranchApplicant> applicantList = actBranchApplicantMapper.selectByExample(applicantExample);
					List<ActBranchApplicantDto> applicantDtoList = DTOConvert.convert2DTO(applicantList, ActBranchApplicantDto.class);
					if (CollectionUtils.isNotEmpty(applicantDtoList)) {
						for (ActBranchApplicantDto applicantDto : applicantDtoList) {
							if (applicantDto.getApplyType() == (byte) ApplyTypeEnum.APPLY_USER.getValue()) {
								CompUserDto applyDto = compUserApi.queryCompUserInfoById(applicantDto.getApplyId(), true);
								if (applyDto != null) {
									applicantDto.setApplyName(applyDto.getName());
								}
							} else if (applicantDto.getApplyType() == (byte) ApplyTypeEnum.APPLY_DEPT.getValue()) {
								List<CompDepartmentDto> deptList = departmentApi.queryDepartmentsByNameAndCompanyId(applicantDto.getApplyId(), null,
										actAuditBranchDto.getCompanyId());
								if (CollectionUtils.isNotEmpty(deptList)) {
									applicantDto.setApplyName(deptList.get(0).getName());
								}
							}
						}
						actAuditBranchDto.setApplicantList(applicantDtoList);
					}
				}
				if (StringUtils.isNotBlank(actAuditBranchDto.getProcDefKey())) {
					ProcBranchResponse branchResp = workflowApi.getProcBranchUsers(actAuditBranchDto.getProcDefKey(),
							actAuditBranchDto.getCompanyId());
					if (branchResp != null) {
						actAuditBranchDto.setBranchUsers(branchResp);
					}
				}
			}
		}
		return dtoList;
	}

	/**
	 * 根据发起人查询可用分支
	 * 
	 * @param startUserId
	 * @param moduleType
	 * @param companyId
	 * @return
	 */
	public ActBranchApplicantDto queryValidAuditBranch(long startUserId, String moduleType, long companyId) {
		// 查询个人分支
		{
			ActBranchApplicantExample example = new ActBranchApplicantExample();
			ActBranchApplicantExample.Criteria criteria = example.createCriteria();
			criteria.andCompanyIdEqualTo(companyId);
			criteria.andApplyIdEqualTo(startUserId);// 根据发起人
			criteria.andApplyTypeEqualTo((byte) ApplyTypeEnum.APPLY_USER.getValue());
			criteria.andModuleTypeEqualTo(moduleType);
			List<ActBranchApplicant> byUserlist = actBranchApplicantMapper.selectByExample(example);
			if (CollectionUtils.isNotEmpty(byUserlist)) {
				List<ActBranchApplicantDto> dtoList = DTOConvert.convert2DTO(byUserlist, ActBranchApplicantDto.class);
				if (CollectionUtils.isNotEmpty(dtoList)) {
					if (StringUtils.isNotBlank(dtoList.get(0).getProcDefKey())) {
						ProcBranchResponse branchResp = workflowApi.getProcBranchUsers(dtoList.get(0).getProcDefKey(), dtoList.get(0).getCompanyId());
						if (branchResp != null) {
							dtoList.get(0).setBranchUsers(branchResp);
						}
					}
				}
				return dtoList.get(0);
			}
		}
		// 查询个人所属部门的分支
		{
			List<CompDepartmentDto> deptList = departmentApi.queryCompUserDepts(startUserId, companyId);
			if (CollectionUtils.isNotEmpty(deptList)) {
				List<Long> deptIds = new ArrayList<Long>();
				for (CompDepartmentDto deptDto : deptList) {
					deptIds.add(deptDto.getId());
				}
				ActBranchApplicantExample example = new ActBranchApplicantExample();
				ActBranchApplicantExample.Criteria criteria = example.createCriteria();
				criteria.andCompanyIdEqualTo(companyId);
				criteria.andApplyIdIn(deptIds);// 根据部门
				criteria.andApplyTypeEqualTo((byte) ApplyTypeEnum.APPLY_DEPT.getValue());
				criteria.andModuleTypeEqualTo(moduleType);
				List<ActBranchApplicant> byDeptlist = actBranchApplicantMapper.selectByExample(example);
				if (CollectionUtils.isNotEmpty(byDeptlist)) {
					List<ActBranchApplicantDto> dtoList = DTOConvert.convert2DTO(byDeptlist, ActBranchApplicantDto.class);
					if (CollectionUtils.isNotEmpty(dtoList)) {
						if (StringUtils.isNotBlank(dtoList.get(0).getProcDefKey())) {
							ProcBranchResponse branchResp = workflowApi.getProcBranchUsers(dtoList.get(0).getProcDefKey(),
									dtoList.get(0).getCompanyId());
							if (branchResp != null) {
								dtoList.get(0).setBranchUsers(branchResp);
							}
						}
					}
					return dtoList.get(0);
				}
			}
		}
		// 查询无前置条件分支
		{
			ActAuditBranchExample example = new ActAuditBranchExample();
			ActAuditBranchExample.Criteria criteria = example.createCriteria();
			criteria.andCompanyIdEqualTo(companyId);
			criteria.andStatusEqualTo((byte) CommonStatus.NOR.getValue());// 启动
			criteria.andPreConditionEqualTo((byte) PreConditionEnum.NO_CONDITION.getValue());
			criteria.andModuleTypeEqualTo(moduleType);
			List<ActAuditBranch> byDeptlist = actAuditBranchMapper.selectByExample(example);
			if (CollectionUtils.isNotEmpty(byDeptlist)) {
				ActBranchApplicantDto applicantDto = new ActBranchApplicantDto();
				List<ActAuditBranchDto> dtoList = DTOConvert.convert2DTO(byDeptlist, ActAuditBranchDto.class);
				if (CollectionUtils.isNotEmpty(dtoList)) {
					if (StringUtils.isNotBlank(dtoList.get(0).getProcDefKey())) {
						applicantDto.setActBranchId(dtoList.get(0).getId());
						applicantDto.setModuleType(dtoList.get(0).getModuleType());
						applicantDto.setProcDefKey(dtoList.get(0).getProcDefKey());
						ProcBranchResponse branchResp = workflowApi.getProcBranchUsers(dtoList.get(0).getProcDefKey(), dtoList.get(0).getCompanyId());
						if (branchResp != null) {
							applicantDto.setBranchUsers(branchResp);
						}
					}
				}
				return applicantDto;
			}
		}
		return null;// 无可用审批分支
	}

	/**
	 * 判断是否存在默认分支
	 * 
	 * @param moduleType
	 * @param companyId
	 * @return
	 */
	public boolean validDefaultBranchExist(String moduleType, long companyId) {
		ActAuditBranchExample example = new ActAuditBranchExample();
		ActAuditBranchExample.Criteria criteria = example.createCriteria();
		criteria.andCompanyIdEqualTo(companyId);
		criteria.andModuleTypeEqualTo(moduleType);
		criteria.andPreConditionEqualTo((byte) PreConditionEnum.NO_CONDITION.getValue());
		int count = actAuditBranchMapper.countByExample(example);
		if (count > 0) {
			return true;
		}
		return false;
	}
}
