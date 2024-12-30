package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.Emp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface EmpMapper {
    Emp getEmpById(String username);
}
