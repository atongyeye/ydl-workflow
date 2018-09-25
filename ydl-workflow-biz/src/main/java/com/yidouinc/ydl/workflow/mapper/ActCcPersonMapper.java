package com.yidouinc.ydl.workflow.mapper;

import com.yidouinc.ydl.workflow.domain.ActCcPerson;
import com.yidouinc.ydl.workflow.domain.ActCcPersonExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActCcPersonMapper {
    int countByExample(ActCcPersonExample example);

    int deleteByExample(ActCcPersonExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ActCcPerson record);

    int insertSelective(ActCcPerson record);

    List<ActCcPerson> selectByExample(ActCcPersonExample example);

    ActCcPerson selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ActCcPerson record, @Param("example") ActCcPersonExample example);

    int updateByExample(@Param("record") ActCcPerson record, @Param("example") ActCcPersonExample example);

    int updateByPrimaryKeySelective(ActCcPerson record);

    int updateByPrimaryKey(ActCcPerson record);
    
    /**
     * 查询抄送人ids
     * 
     * @param moduleType
     * @param moduleId
     * @param procInstId
     * @param companyId
     * @return
     */
    List<Long> selectCcPersonIdsByModuleId(@Param("moduleType") String moduleType,@Param("moduleId") Long moduleId,@Param("procInstId") String procInstId,@Param("companyId") Long companyId);
}