/**
 * 
 */
package com.yidouinc.ydl.workflow.service;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yidouinc.contract.api.ContractApi;
import com.yidouinc.contract.dto.ContractDto;
import com.yidouinc.mars.enums.YdlModuleTypeEnum;
import com.yidouinc.mars.response.OperResult;
import com.yidouinc.ydl.workflow.domain.ActBusinessForm;
import com.yidouinc.ydl.workflow.domain.ActBusinessFormExample;
import com.yidouinc.ydl.workflow.dto.ProcInstanceDto;
import com.yidouinc.ydl.workflow.mapper.ActBusinessFormMapper;

/**
 * @author angq 业务表单
 *
 */
@Service
@Transactional
public class ActBusinessFormService {

	private static Logger log = LoggerFactory.getLogger(ActBusinessFormService.class);

	@Autowired
	private ActBusinessFormMapper actBusinessFormMapper;

	@Autowired
	private ContractApi contractApi;

	/**
	 * 发起或重新发起流程,保存业务表单
	 * 
	 * @param dto
	 */
	public OperResult saveBusinessForm(ProcInstanceDto dto) {
		ActBusinessForm businessForm = new ActBusinessForm();
		businessForm.setCompanyId(dto.getCompanyId());
		businessForm.setProcInstId(dto.getProcInstId());
		businessForm.setOperatingFormId(dto.getOperatingFormId());
		businessForm.setCreatorId(dto.getOperatorId());
		if (dto.getBusinessType().equals(YdlModuleTypeEnum.CONTRACT.getValue())) {
			ContractDto contractDto = contractApi.queryContractDetailForBusinessForm(Long.valueOf(dto.getBusinessKey()));
			if (contractDto != null) {
				businessForm.setBusinessForm(ObjectToByte(contractDto));
			}
		}
		actBusinessFormMapper.insertSelective(businessForm);
		return OperResult.getSuccessResult(businessForm.getId());
	}

	/**
	 * 根据审批单id和流程实例id唯一确定一个业务表单
	 * 
	 * @param dto
	 * @return
	 */
	public ActBusinessForm queryBusinessForm(String procInstId,Long operatingFormId, Long companyId) {
		ActBusinessFormExample example = new ActBusinessFormExample();
		ActBusinessFormExample.Criteria criteria = example.createCriteria();
		criteria.andCompanyIdEqualTo(companyId);
		criteria.andProcInstIdEqualTo(procInstId);
		criteria.andOperatingFormIdEqualTo(operatingFormId);
		List<ActBusinessForm> businessFormList = actBusinessFormMapper.selectByExampleWithBLOBs(example);
		if(CollectionUtils.isNotEmpty(businessFormList)){
			return businessFormList.get(0);
		}
		return null;
	}
	
	/**
	 * 序列化业务表单
	 * 
	 * @param obj
	 * @return
	 */
	private byte[] ObjectToByte(Object obj) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream outputStream = new ObjectOutputStream(out);
			outputStream.writeObject(obj);
			byte[] bytes = out.toByteArray();
			outputStream.close();
			return bytes;
		} catch (Exception e) {
			log.info("序列化失败:" + e);
			return null;
		}
	}
}
