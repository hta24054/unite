package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.Dept;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeptMapper {
    Dept getDeptByEmpId(String empId);
}
