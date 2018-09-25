package com.yidouinc.ydl.workflow.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yidouinc.ydl.workflow.domain.ActBranchCc;
import com.yidouinc.ydl.workflow.domain.ActBranchCcExample;

public interface ActBranchCcMapper {
	int countByExample(ActBranchCcExample example);

	int deleteByExample(ActBranchCcExample example);

	int deleteByPrimaryKey(Long id);

	int insert(ActBranchCc record);

	int insertSelective(ActBranchCc record);

	List<ActBranchCc> selectByExample(ActBranchCcExample example);

	ActBranchCc selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") ActBranchCc record, @Param("example") ActBranchCcExample example);

	int updateByExample(@Param("record") ActBranchCc record, @Param("example") ActBranchCcExample example);

	int updateByPrimaryKeySelective(ActBranchCc record);

	int updateByPrimaryKey(ActBranchCc record);

	/**
	 * 查询分支抄送人ids
	 * 
	 * @param procDefKey
	 * @param companyId
	 * @return
	 */
	List<Long> selectCcPersonIdsByProcDefKey(@Param("procDefKey") String procDefKey, @Param("companyId") Long companyId);
	
	/**
	 * 删除分支抄送人信息
	 * 
	 * @param branchId
	 * @param companyId
	 * @return
	 */
	int deleteByBranchId(@Param("branchId") long branchId, @Param("companyId") long companyId);
	
	/**
	 * 查询分支抄送人列表
	 * 
	 * @param procDefKey
	 * @param moduleType
	 * @param companyId
	 * @return
	 */
	List<Long> queryBranchCcList(@Param("procDefKey") String procDefKey, @Param("moduleType") String moduleType,
			@Param("companyId") long companyId);
}