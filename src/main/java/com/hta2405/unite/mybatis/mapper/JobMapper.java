package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.Job;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface JobMapper {
    Job getJobByEmpId(String empId);

    List<Job> getAllJob();
}
