package com.hta2405.unite.service;

import com.hta2405.unite.domain.Dept;
import com.hta2405.unite.mybatis.mapper.DeptMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeptService {
    private final DeptMapper deptMapper;

    public List<Dept> getSubDeptList(Long deptId) {
        return deptMapper.getSubDeptList(deptId);
    }

    public List<Dept> getAllDept() {
        return deptMapper.getAllDept();
    }
}
