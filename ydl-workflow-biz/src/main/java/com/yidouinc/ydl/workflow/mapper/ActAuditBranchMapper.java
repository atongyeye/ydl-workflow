package com.yidouinc.ydl.workflow.mapper;

import com.yidouinc.ydl.workflow.domain.ActAuditBranch;
import com.yidouinc.ydl.workflow.domain.ActAuditBranchExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActAuditBranchMapper {
    int countByExample(ActAuditBranchExample example);

    int deleteByExample(ActAuditBranchExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ActAuditBranch record);

    int insertSelective(ActAuditBranch record);

    List<ActAuditBranch> selectByExample(ActAuditBranchExample example);

    ActAuditBranch selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ActAuditBranch record, @Param("example") ActAuditBranchExample example);

    int updateByExample(@Param("record") ActAuditBranch record, @Param("example") ActAuditBranchExample example);

    int updateByPrimaryKeySelective(ActAuditBranch record);

    int updateByPrimaryKey(ActAuditBranch record);
}