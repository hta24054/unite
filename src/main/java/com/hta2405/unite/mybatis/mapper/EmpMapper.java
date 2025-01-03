package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.dto.EmpTreeDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface EmpMapper {
    Emp getEmpById(String empId);

    int update(Emp emp);

    String getImgOriginal(String fileUUID);

    List<EmpTreeDTO> getEmpListByDeptId(Long deptId);

    List<EmpTreeDTO> getEmpListByName(String ename);
}
