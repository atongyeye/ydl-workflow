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

import com.yidouinc.mars.response.OperResult;
import com.yidouinc.ydl.workflow.domain.ActCcPerson;
import com.yidouinc.ydl.workflow.domain.ActCcPersonExample;
import com.yidouinc.ydl.workflow.dto.ProcInstanceDto;
import com.yidouinc.ydl.workflow.mapper.ActBranchCcMapper;
import com.yidouinc.ydl.workflow.mapper.ActCcPersonMapper;

/**
 * @author angq 抄送人
 *
 */
@Service
@Transactional
public class ActCcPersonService {

	@Autowired
	private ActCcPersonMapper actCcPersonMapper;

	@Autowired
	private ActBranchCcMapper actBranchCcMapper;
	
	/**
	 * 发起或重新发起流程，保存抄送人信息
	 * 
	 * @param dto
	 */
	public OperResult saveCcPerson(ProcInstanceDto dto) {
		List<Long> ccIds = actBranchCcMapper.queryBranchCcList(dto.getProcDefKey(), dto.getBusinessType(), dto.getCompanyId());
		//查询分支抄送人列表
		if (CollectionUtils.isNotEmpty(ccIds)) {
			for (Long personId : ccIds) {
				ActCcPerson person = new ActCcPerson();
				person.setCompanyId(dto.getCompanyId());
				person.setCreatorId(dto.getOperatorId());
				person.setModuleId(Long.valueOf(dto.getBusinessKey()));
				person.setModuleType(dto.getBusinessType());
				person.setPersonId(personId);
				person.setOperatingFormId(dto.getOperatingFormId());
				person.setProcInstId(dto.getProcInstId());
				actCcPersonMapper.insertSelective(person);
			}
		}
		return OperResult.getSuccessResult();
	}

	/**
	 * 根据moduleId和moduleType,流程实例id 查询抄送人列表
	 * 
	 * @param dto
	 * @return
	 */
	public List<ActCcPerson> queryCcPersonList(ProcInstanceDto dto) {
		ActCcPersonExample example = new ActCcPersonExample();
		ActCcPersonExample.Criteria criteria = example.createCriteria();
		criteria.andCompanyIdEqualTo(dto.getCompanyId());
		criteria.andModuleIdEqualTo(Long.valueOf(dto.getBusinessKey()));
		criteria.andModuleTypeEqualTo(dto.getBusinessType());
		if(StringUtils.isNotBlank(dto.getProcInstId())){
			criteria.andProcInstIdEqualTo(dto.getProcInstId());
		}
		List<ActCcPerson> personList = actCcPersonMapper.selectByExample(example);
		return personList;
	}

	/**
	 * 查询抄送人ids
	 * 
	 * @param moduleType
	 * @param moduleId
	 * @param procInstId
	 * @param companyId
	 * @return
	 */
	public List<Long> queryCcPersonIds(String moduleType, Long moduleId,String procInstId, Long companyId) {
		return actCcPersonMapper.selectCcPersonIdsByModuleId(moduleType, moduleId, procInstId,companyId);
	}

}
