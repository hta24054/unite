package com.hta2405.unite.service;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.mybatis.mapper.EmpMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmpService {
    private final EmpMapper empMapper;

    public Optional<Emp> getEmpById(String empId) {
        return empMapper.getEmpById(empId);
    }
}
