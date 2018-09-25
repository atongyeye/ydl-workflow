package com.yidouinc.ydl.workflow.mapper;

import com.yidouinc.ydl.workflow.domain.ActBranchApplicant;
import com.yidouinc.ydl.workflow.domain.ActBranchApplicantExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActBranchApplicantMapper {
    int countByExample(ActBranchApplicantExample example);

    int deleteByExample(ActBranchApplicantExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ActBranchApplicant record);

    int insertSelective(ActBranchApplicant record);

    List<ActBranchApplicant> selectByExample(ActBranchApplicantExample example);

    ActBranchApplicant selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ActBranchApplicant record, @Param("example") ActBranchApplicantExample example);

    int updateByExample(@Param("record") ActBranchApplicant record, @Param("example") ActBranchApplicantExample example);

    int updateByPrimaryKeySelective(ActBranchApplicant record);

    int updateByPrimaryKey(ActBranchApplicant record);
}