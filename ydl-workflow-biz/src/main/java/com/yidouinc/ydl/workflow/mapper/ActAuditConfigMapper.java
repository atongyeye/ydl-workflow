package com.yidouinc.ydl.workflow.mapper;

import com.yidouinc.ydl.workflow.domain.ActAuditConfig;
import com.yidouinc.ydl.workflow.domain.ActAuditConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActAuditConfigMapper {
    int countByExample(ActAuditConfigExample example);

    int deleteByExample(ActAuditConfigExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ActAuditConfig record);

    int insertSelective(ActAuditConfig record);

    List<ActAuditConfig> selectByExample(ActAuditConfigExample example);

    ActAuditConfig selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ActAuditConfig record, @Param("example") ActAuditConfigExample example);

    int updateByExample(@Param("record") ActAuditConfig record, @Param("example") ActAuditConfigExample example);

    int updateByPrimaryKeySelective(ActAuditConfig record);

    int updateByPrimaryKey(ActAuditConfig record);
}