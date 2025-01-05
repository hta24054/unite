package com.hta2405.unite.service;

import com.hta2405.unite.domain.Cert;
import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.Lang;
import com.hta2405.unite.dto.*;
import com.hta2405.unite.mybatis.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    public Emp getEmpById(String empId) {
        return empMapper.getEmpById(empId);
    }

    @Transactional
    public EmpInfoDTO getEmpInfoDTO(String empId) {
        Emp emp = getEmpById(empId);
        return EmpInfoDTO.builder()
                .emp(emp)
                .deptList(deptMapper.getAllDept())
                .jobList(jobMapper.getAllJob())
                .langList(langMapper.getLangByEmpId(empId))
                .certList(certMapper.getCertByEmpId(empId)).build();
    }

    @Transactional
    public Emp registerEmp(EmpRegisterDTO empRegisterDTO, MultipartFile file) {
        // 1. 파일 업로드
        FileDTO fileDTO = profileImgService.insertProfileImg(file);

        // 2. empRegisterDTO -> emp Entity로 변환
        Emp emp = empRegisterDTO.toEntity(fileDTO);

        // 3. emp 테이블에 입력
        empMapper.insertEmp(emp);


        // 4. 자격증 및 외국어능력 입력
        List<Lang> langList = empRegisterDTO.getLang();
        List<Cert> certList = empRegisterDTO.getCert();

        if (langList != null && !langList.isEmpty()) {
            langMapper.insertLang(langList);
        }

        if (certList != null && !certList.isEmpty()) {
            certMapper.insertCert(certList);
        }

        return emp;
    }

    @Transactional
    public int updateEmpByAdmin(String empId, MultipartFile file, EmpAdminUpdateDTO dto) {
        Emp emp = empMapper.getEmpById(empId);
        FileDTO fileDTO = profileImgService.updateProfileImg(file, dto.getBeforeFileName(), emp);
        emp.updateByAdmin(dto, fileDTO);
        updateLangAndCert(dto);
        return empMapper.updateEmp(emp);
    }

    @Transactional
    public int updateEmpBySelf(String empId, MultipartFile file, EmpSelfUpdateDTO dto) {
        Emp emp = empMapper.getEmpById(empId);
        FileDTO fileDTO = profileImgService.updateProfileImg(file, dto.getBeforeFileName(), emp);
        emp.updateBySelf(dto, fileDTO);
        return empMapper.updateEmp(emp);
    }


    private void updateLangAndCert(EmpAdminUpdateDTO dto) {
        langMapper.deleteAllLangByEmpId(dto.getEmpId());
        certMapper.deleteAllCertByEmpId(dto.getEmpId());
        List<Lang> langList = dto.getLang();
        List<Cert> certList = dto.getCert();

        if (langList != null && !langList.isEmpty()) {
            langMapper.insertLang(langList);
        }

        if (certList != null && !certList.isEmpty()) {
            certMapper.insertCert(certList);
        }
    }

    public List<EmpTreeDTO> getEmpListByDeptId(Long deptId) {
        return empMapper.getHiredEmpListByDeptId(deptId);
    }

    public List<EmpTreeDTO> getEmpListByName(String ename) {
        return empMapper.getEmpListByName(ename);
    }

    public RegisterDTO getRegisterPageData() {
        return new RegisterDTO(deptMapper.getAllDept(), jobMapper.getAllJob());
    }

    public void resignEmp(String empId) {
        empMapper.resignEmp(empId);
    }
}
