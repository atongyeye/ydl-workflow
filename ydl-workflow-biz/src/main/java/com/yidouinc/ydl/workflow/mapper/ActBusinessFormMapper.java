package com.yidouinc.ydl.workflow.mapper;

import com.yidouinc.ydl.workflow.domain.ActBusinessForm;
import com.yidouinc.ydl.workflow.domain.ActBusinessFormExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActBusinessFormMapper {
    int countByExample(ActBusinessFormExample example);

    int deleteByExample(ActBusinessFormExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ActBusinessForm record);

    int insertSelective(ActBusinessForm record);

    List<ActBusinessForm> selectByExampleWithBLOBs(ActBusinessFormExample example);

    List<ActBusinessForm> selectByExample(ActBusinessFormExample example);

    ActBusinessForm selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ActBusinessForm record, @Param("example") ActBusinessFormExample example);

    int updateByExampleWithBLOBs(@Param("record") ActBusinessForm record, @Param("example") ActBusinessFormExample example);

    int updateByExample(@Param("record") ActBusinessForm record, @Param("example") ActBusinessFormExample example);

    int updateByPrimaryKeySelective(ActBusinessForm record);

    int updateByPrimaryKeyWithBLOBs(ActBusinessForm record);

    int updateByPrimaryKey(ActBusinessForm record);
}