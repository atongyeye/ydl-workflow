package com.yidouinc.ydl.workflow.mapper;

import com.yidouinc.ydl.workflow.domain.ActOperating;
import com.yidouinc.ydl.workflow.domain.ActOperatingExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActOperatingMapper {
    int countByExample(ActOperatingExample example);

    int deleteByExample(ActOperatingExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ActOperating record);

    int insertSelective(ActOperating record);

    List<ActOperating> selectByExample(ActOperatingExample example);

    ActOperating selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ActOperating record, @Param("example") ActOperatingExample example);

    int updateByExample(@Param("record") ActOperating record, @Param("example") ActOperatingExample example);

    int updateByPrimaryKeySelective(ActOperating record);

    int updateByPrimaryKey(ActOperating record);
    
    /**
     * 查询操作人ids
     * 
     * @param procInstId
     * @param operatingFormId
     * @param companyId
     * @return
     */
    List<Long> selectOperatorIdsByProcInstId(@Param("procInstId") String procInstId,@Param("operatingFormId") Long operatingFormId,@Param("companyId") Long companyId);
    
    /**
     * 查询最近操作记录
     * 
     * @param procInstId
     * @param operatingFormId
     * @param companyId
     * @return
     */
    ActOperating selectLastOperatingByProcInstId(@Param("procInstId") String procInstId,@Param("operatingFormId") Long operatingFormId,@Param("companyId") Long companyId);
    
    /**
     * 查询是否有状态为审批中的操作
     * 
     * @param operatingFormId
     * @param companyId
     * @return
     */
    int selectApprovedByOperatingFormId(@Param("operatingFormId") Long operatingFormId,@Param("companyId") Long companyId);
}