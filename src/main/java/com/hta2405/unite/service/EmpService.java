package com.hta2405.unite.service;

import com.hta2405.unite.domain.Cert;
import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.Lang;
import com.hta2405.unite.dto.EmpAdminUpdateDTO;
import com.hta2405.unite.dto.EmpInfoDTO;
import com.hta2405.unite.dto.EmpSelfUpdateDTO;
import com.hta2405.unite.dto.FileDTO;
import com.hta2405.unite.mybatis.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmpService {
    private final ProfileImgService profileImgService;
    private final EmpMapper empMapper;
    private final LangMapper langMapper;
    private final CertMapper certMapper;
    private final JobMapper jobMapper;
    private final DeptMapper deptMapper;

    public Optional<Emp> getEmpById(String empId) {
        return empMapper.getEmpById(empId);
    }

    @Transactional
    public EmpInfoDTO getEmpInfoDTO(String empId) {
        Emp emp = getEmpById(empId).orElseThrow(
                () -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));

        return EmpInfoDTO.builder()
                .emp(emp)
                .deptList(deptMapper.getAllDept())
                .jobList(jobMapper.getAllJob())
                .langList(langMapper.getLangByEmpId(empId))
                .certList(certMapper.getCertByEmpId(empId)).build();
    }

    @Transactional
    public int updateEmpByAdmin(String empId, MultipartFile file, EmpAdminUpdateDTO dto) {
        Emp emp = empMapper.getEmpById(empId)
                .orElseThrow(() -> new UsernameNotFoundException("유저 정보 없음"));
        FileDTO fileDTO = profileImgService.changeImg(file, dto.getBeforeFileName(), emp);
        emp.updateByAdmin(dto, fileDTO);
        updateLangAndCert(dto);
        return empMapper.update(emp);
    }

    @Transactional
    public int updateEmpBySelf(String empId, MultipartFile file, EmpSelfUpdateDTO dto) {
        Emp emp = empMapper.getEmpById(empId)
                .orElseThrow(() -> new UsernameNotFoundException("유저 정보 없음"));
        FileDTO fileDTO = profileImgService.changeImg(file, dto.getBeforeFileName(), emp);
        emp.updateBySelf(dto, fileDTO);
        return empMapper.update(emp);
    }

    private void updateLangAndCert(EmpAdminUpdateDTO dto) {
        langMapper.deleteAllLangByEmpId(dto.getEmpId());
        certMapper.deleteAllCertByEmpId(dto.getEmpId());
        List<Lang> langList = dto.getLang();
        List<Cert> certList = dto.getCert();
        if (!langList.isEmpty()) {
            langMapper.insertLang(langList);
        }
        if (!certList.isEmpty()) {
            certMapper.insertCert(certList);
        }
    }
}
