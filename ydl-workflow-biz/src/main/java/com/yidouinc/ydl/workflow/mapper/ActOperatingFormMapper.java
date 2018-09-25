package com.yidouinc.ydl.workflow.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yidouinc.ydl.workflow.domain.ActOperatingForm;
import com.yidouinc.ydl.workflow.domain.ActOperatingFormExample;

public interface ActOperatingFormMapper {
    int countByExample(ActOperatingFormExample example);

    int deleteByExample(ActOperatingFormExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ActOperatingForm record);

    int insertSelective(ActOperatingForm record);

    List<ActOperatingForm> selectByExample(ActOperatingFormExample example);

    ActOperatingForm selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ActOperatingForm record, @Param("example") ActOperatingFormExample example);

    int updateByExample(@Param("record") ActOperatingForm record, @Param("example") ActOperatingFormExample example);

    int updateByPrimaryKeySelective(ActOperatingForm record);

    int updateByPrimaryKey(ActOperatingForm record);
    
    /**
     * @param moduleId
     * @param moduleType
     * @param companyId
     * @return
     */
    ActOperatingForm selectOperatingFormByModuleId(@Param("moduleId") long moduleId,@Param("moduleType") String moduleType,@Param("companyId") long companyId);
}