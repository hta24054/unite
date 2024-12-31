package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.Job;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JobMapper {
    Job getJobByEmpId(String empId);
}
