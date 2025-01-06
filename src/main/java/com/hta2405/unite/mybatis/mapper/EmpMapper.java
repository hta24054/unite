package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.Dept;
import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.dto.EmpListDTO;
import com.hta2405.unite.dto.EmpTreeDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmpMapper {
    Emp getEmpById(String empId);

    int updateEmp(Emp emp);

    String getImgOriginal(String fileUUID);

    List<EmpTreeDTO> getHiredEmpListByDeptId(Long deptId);

    List<EmpTreeDTO> getHiredEmpListByName(String ename);

    int insertEmp(Emp emp);

    void resignEmp(String empId);

    List<EmpListDTO> getEmpListDTO(List<Dept> list);
}
