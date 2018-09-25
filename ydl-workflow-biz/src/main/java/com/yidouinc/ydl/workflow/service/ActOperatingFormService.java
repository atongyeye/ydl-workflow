/**
 * 
 */
package com.yidouinc.ydl.workflow.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yidouinc.mars.common.utils.DTOConvert;
import com.yidouinc.mars.enums.CodeTypes;
import com.yidouinc.mars.enums.YdlModuleTypeEnum;
import com.yidouinc.mars.response.OperResult;
import com.yidouinc.ydl.workflow.domain.ActCodeKey;
import com.yidouinc.ydl.workflow.domain.ActOperatingForm;
import com.yidouinc.ydl.workflow.domain.ActOperatingFormExample;
import com.yidouinc.ydl.workflow.dto.ActOperatingFormDto;
import com.yidouinc.ydl.workflow.dto.ProcInstanceDto;
import com.yidouinc.ydl.workflow.mapper.ActCodeMapper;
import com.yidouinc.ydl.workflow.mapper.ActOperatingFormMapper;

/**
 * @author angq 审批单
 *
 */
@Service
@Transactional
public class ActOperatingFormService {

	@Autowired
	private ActCodeMapper actCodeMapper;

	@Autowired
	private ActOperatingFormMapper actOperatingFormMapper;

	/**
	 * 发起流程,保存审批单
	 * 
	 * @param dto
	 */
	public OperResult saveOperatingForm(ProcInstanceDto dto) {
		// TODO多次审批，同一个审批单号？
		ActOperatingFormExample example = new ActOperatingFormExample();
		ActOperatingFormExample.Criteria criteria = example.createCriteria();
		criteria.andCompanyIdEqualTo(dto.getCompanyId());
		criteria.andModuleIdEqualTo(Long.valueOf(dto.getBusinessKey()));
		criteria.andModuleTypeEqualTo(dto.getBusinessType());
		List<ActOperatingForm> list = actOperatingFormMapper.selectByExample(example);
		if (CollectionUtils.isNotEmpty(list)) {// 业务对象已存在,直接返回审批单id
			return OperResult.getSuccessResult(list.get(0).getId());
		}
		ActCodeKey actCode = new ActCodeKey();
		actCode.setCompanyId(dto.getCompanyId());
		actCodeMapper.insertSelective(actCode);
		if (dto.getBusinessType().equals(YdlModuleTypeEnum.CONTRACT.getValue())) {
			ActOperatingForm operatingForm = new ActOperatingForm();
			operatingForm.setCompanyId(dto.getCompanyId());
			operatingForm.setModuleId(Long.valueOf(dto.getBusinessKey()));
			operatingForm.setModuleType(YdlModuleTypeEnum.CONTRACT.getValue());
			operatingForm.setCode(generateAppNo(actCode.getSequenceCode()));
			actOperatingFormMapper.insertSelective(operatingForm);
			return OperResult.getSuccessResult(operatingForm.getId());
		}
		return null;
	}

	/**
	 * 根据moduleId和moduleType 查询审批单
	 * 
	 * @param moduleId
	 * @param moduleType
	 * @param companyId
	 * @return
	 */
	public ActOperatingFormDto queryOperatingForm(Long moduleId,String moduleType,Long companyId) {
		ActOperatingFormExample example = new ActOperatingFormExample();
		ActOperatingFormExample.Criteria criteria = example.createCriteria();
		criteria.andCompanyIdEqualTo(companyId);
		criteria.andModuleIdEqualTo(moduleId);
		criteria.andModuleTypeEqualTo(moduleType);
		List<ActOperatingForm> list = actOperatingFormMapper.selectByExample(example);
		List<ActOperatingFormDto> dtoList = DTOConvert.convert2DTO(list, ActOperatingFormDto.class);
		if (CollectionUtils.isNotEmpty(dtoList)) {
			return dtoList.get(0);
		}
		return null;
	}
	
	/**
	 * 生成审批编号(当不足4位时高位补0)
	 * 
	 * @param sequeueCode
	 * @return
	 */
	private String generateAppNo(int sequeueCode) {
		String result;
		if (sequeueCode >= 1000) {
			result = sequeueCode + "";
		} else {
			int length = (sequeueCode + "").length();
			if (length == 1) {
				result = "000" + sequeueCode;
			} else if (length == 2) {
				result = "00" + sequeueCode;
			} else {
				result = "0" + sequeueCode;
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		result = CodeTypes.WORKFLOW_CODE.getType() + sdf.format(new Date()) + result;
		return result;
	}
}
