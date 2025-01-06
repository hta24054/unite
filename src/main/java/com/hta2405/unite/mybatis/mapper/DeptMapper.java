package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.Dept;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DeptMapper {
    Dept getDeptByEmpId(String empId);

    List<Dept> getAllDept();

    List<Dept> getSubDeptList(Long deptId);
}
