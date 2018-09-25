/**
 * 
 */
package com.yidouinc.ydl.workflow.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yidouinc.mars.common.utils.DTOConvert;
import com.yidouinc.mars.enums.CommonStatus;
import com.yidouinc.mars.response.OperResult;
import com.yidouinc.ydl.workflow.domain.ActAuditBranch;
import com.yidouinc.ydl.workflow.domain.ActAuditConfig;
import com.yidouinc.ydl.workflow.domain.ActAuditConfigExample;
import com.yidouinc.ydl.workflow.dto.ActAuditConfigDto;
import com.yidouinc.ydl.workflow.enums.PreConditionEnum;
import com.yidouinc.ydl.workflow.mapper.ActAuditBranchMapper;
import com.yidouinc.ydl.workflow.mapper.ActAuditConfigMapper;
import com.yidouinc.ydl.workflow.query.ActConfigQuery;

/**
 * @author angq 审批配置
 *
 */
@Service
@Transactional
public class ActAuditConfigService {

	@Autowired
	private ActAuditConfigMapper actAuditConfigMapper;

	@Autowired
	private ActAuditBranchMapper actAuditBranchMapper;
	
	/**
	 * 保存审批配置
	 * 
	 * @param dto
	 */
	public OperResult saveAuditConfig(ActAuditConfigDto dto) {
		ActAuditConfigExample example = new ActAuditConfigExample();
		ActAuditConfigExample.Criteria criteria = example.createCriteria();
		criteria.andCompanyIdEqualTo(dto.getCompanyId());
		criteria.andModuleTypeEqualTo(dto.getModuleType());
		List<ActAuditConfig> list = actAuditConfigMapper.selectByExample(example);
		ActAuditConfig auditConfig = null;
		if (CollectionUtils.isEmpty(list)) {// 不存在则保存
			auditConfig = DTOConvert.convert2DTO(dto, ActAuditConfig.class);
			actAuditConfigMapper.insertSelective(auditConfig);
			//创建默认分支
			ActAuditBranch actAuditBranch = new ActAuditBranch();
			actAuditBranch.setName("默认分支");
			actAuditBranch.setPreCondition((byte) PreConditionEnum.NO_CONDITION.getValue());
			actAuditBranch.setStatus(dto.getStatus());
			actAuditBranch.setUpdatorId(dto.getCreatorId());
			actAuditBranch.setCreatorId(dto.getCreatorId());
			actAuditBranch.setActConfigId(auditConfig.getId());
			actAuditBranch.setCompanyId(dto.getCompanyId());
			actAuditBranch.setModuleType(auditConfig.getModuleType());
			actAuditBranchMapper.insertSelective(actAuditBranch);
		} else {// 存在则更新
			auditConfig = list.get(0);
			auditConfig.setStatus(dto.getStatus());
			auditConfig.setUpdatorId(dto.getUpdatorId());
			actAuditConfigMapper.updateByPrimaryKeySelective(auditConfig);
		}
		return OperResult.getSuccessResult(auditConfig.getId());
	}

	/**
	 * 查询审批配置列表
	 * 
	 * @param dto
	 * @return
	 */
	public List<ActAuditConfigDto> queryAuditConfigList(ActConfigQuery query) {
		ActAuditConfigExample example = new ActAuditConfigExample();
		ActAuditConfigExample.Criteria criteria = example.createCriteria();
		criteria.andCompanyIdEqualTo(query.getCompanyId());
		if (StringUtils.isNotBlank(query.getModuleType())) {
			criteria.andModuleTypeEqualTo(query.getModuleType());
		}
		List<ActAuditConfig> list = actAuditConfigMapper.selectByExample(example);
		List<ActAuditConfigDto> dtoList = DTOConvert.convert2DTO(list, ActAuditConfigDto.class);
		return dtoList;
	}

	/**
	 * 判断审批配置是否开启
	 * 
	 * @param query
	 * @return
	 */
	public boolean queryAuditConfigStautus(String moduleType, long companyId) {
		ActAuditConfigExample example = new ActAuditConfigExample();
		ActAuditConfigExample.Criteria criteria = example.createCriteria();
		criteria.andCompanyIdEqualTo(companyId);
		criteria.andModuleTypeEqualTo(moduleType);
		criteria.andStatusEqualTo((byte) CommonStatus.NOR.getValue());
		int count = actAuditConfigMapper.countByExample(example);
		if (count > 0) {
			return true;
		}
		return false;
	}
}
