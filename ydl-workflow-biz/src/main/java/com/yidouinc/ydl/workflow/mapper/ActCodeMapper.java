package com.yidouinc.ydl.workflow.mapper;

import com.yidouinc.ydl.workflow.domain.ActCodeExample;
import com.yidouinc.ydl.workflow.domain.ActCodeKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActCodeMapper {
    int countByExample(ActCodeExample example);

    int deleteByExample(ActCodeExample example);

    int deleteByPrimaryKey(ActCodeKey key);

    int insert(ActCodeKey record);

    int insertSelective(ActCodeKey record);

    List<ActCodeKey> selectByExample(ActCodeExample example);

    int updateByExampleSelective(@Param("record") ActCodeKey record, @Param("example") ActCodeExample example);

    int updateByExample(@Param("record") ActCodeKey record, @Param("example") ActCodeExample example);
}