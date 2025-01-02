package com.hta2405.unite.service;

import com.hta2405.unite.command.UpdateEmpCommand;
import com.hta2405.unite.domain.Cert;
import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.Lang;
import com.hta2405.unite.dto.EmpInfoDTO;
import com.hta2405.unite.dto.EmpUpdateDTO;
import com.hta2405.unite.mybatis.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmpService {
    private final EmpMapper empMapper;
    private final LangMapper langMapper;
    private final CertMapper certMapper;
    private final JobMapper jobMapper;
    private final DeptMapper deptMapper;

    public Optional<Emp> getEmpById(String empId) {
        return empMapper.getEmpById(empId);
    }

    public EmpInfoDTO getEmpInfoDTO(String empId) {
        Emp emp = getEmpById(empId).orElseThrow(
                () -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));
        String deptName = deptMapper.getDeptByEmpId(empId).getDeptName();
        String jobName = jobMapper.getJobByEmpId(empId).getJobName();
        List<Lang> langList = langMapper.getLangByEmpId(empId);
        List<Cert> certList = certMapper.getCertByEmpId(empId);
        return EmpInfoDTO.builder()
                .emp(emp)
                .deptName(deptName)
                .jobName(jobName)
                .langList(langList)
                .certList(certList).build();
    }

    public int updateEmp(String empId, EmpUpdateDTO dto) {
        Emp emp = empMapper.getEmpById(empId)
                .orElseThrow(() -> new UsernameNotFoundException("유저 정보 없음"));
        UpdateEmpCommand command = dto.createCommand();
        emp = command.apply(emp);
        return empMapper.update(emp);
    }
}
