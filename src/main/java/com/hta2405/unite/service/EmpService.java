package com.hta2405.unite.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hta2405.unite.domain.Cert;
import com.hta2405.unite.domain.Dept;
import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.Lang;
import com.hta2405.unite.dto.*;
import com.hta2405.unite.mybatis.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.hta2405.unite.enums.Role.ROLE_ADMIN;
import static com.hta2405.unite.enums.Role.ROLE_MANAGER;

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
    private final long HR_DEPT_ID = 1120L;
    private static final String REDIS_KEY_PREFIX = "emp:";
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper; // JSON 직렬화/역직렬화

    public Emp getEmpById(String empId) {
        return empMapper.getEmpById(empId);
    }

    public Map<String, String> getIdToENameMap() {
        String redisKey = REDIS_KEY_PREFIX + "map";
        try {
            String cachedData = redisTemplate.opsForValue().get(redisKey);
            if (cachedData != null) {
                log.info("Redis cache hit : {}", redisKey);
                return objectMapper.readValue(cachedData, new TypeReference<>() {
                });
            }
        } catch (Exception e) {
            log.error("Redis 서버 error", e);
        }
        log.info("Redis cache miss, key = {}", redisKey);

        List<Map<String, Object>> resultList = empMapper.getIdToENameMap();

        Map<String, String> resultMap = new HashMap<>();
        for (Map<String, Object> row : resultList) {
            String empId = String.valueOf(row.get("emp_id"));
            String eName = (String) row.get("ename");
            resultMap.put(empId, eName);
        }

        try {
            redisTemplate.opsForValue().set(redisKey, objectMapper.writeValueAsString(resultMap));
        } catch (Exception e) {
            log.error("Redis cache error: {}", redisKey, e);
        }
        return resultMap;
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

        if (!ObjectUtils.isEmpty(langList)) {
            langMapper.insertLang(langList);
        }

        if (!ObjectUtils.isEmpty(certList)) {
            certMapper.insertCert(certList);
        }
        deleteEnameCache();

        return emp;
    }

    private void deleteEnameCache() {
        String redisKey = REDIS_KEY_PREFIX + "map";
        try {
            redisTemplate.delete(redisKey);
        } catch (Exception e) {
            log.error("Redis 서버 오류 또는 캐시 삭제 오류, key : {}", redisKey, e);
        }
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

        if (!ObjectUtils.isEmpty(langList)) {
            langMapper.insertLang(langList);
        }

        if (!ObjectUtils.isEmpty(certList)) {
            certMapper.insertCert(certList);
        }
    }

    public List<EmpListDTO> getEmpListByDeptId(Long deptId) {
        return empMapper.getHiredEmpListByDeptId(deptId);
    }

    public List<EmpListDTO> getEmpListByName(String ename) {
        return empMapper.getHiredEmpListByName(ename);
    }

    public RegisterDTO getRegisterPageData() {
        return new RegisterDTO(deptMapper.getAllDept(), jobMapper.getAllJob());
    }

    public int resignEmp(String empId) {
        return empMapper.resignEmp(empId);
    }

    public List<EmpListDTO> getEmpListDTOByDeptList(List<Dept> list) {
        return empMapper.getHiredEmpListDTO(list);
    }

    public List<EmpListDTO> getAllEmpListDTO() {
        return empMapper.getAllEmpListDTO();
    }


    /**
     * 유저 리스트 테이블 ModelAndView 반환 메서드
     *
     * @param title   : 페이지 제목
     * @param message : 페이지 본문에 표시되는 내용
     * @param empList : 직원 명단
     * @param url     : 직원 이름을 클릭했을 때 이동하는 URL
     */
    public ModelAndView showEmpList(ModelAndView mv, String title, String message, List<EmpListDTO> empList, String url) {
        mv.addObject("title", title);
        mv.addObject("message", message);
        mv.addObject("empList", empList);
        mv.addObject("url", url);
        mv.setViewName("emp/empList");
        return mv;
    }

    // 관리자) 내 하위부서 직원인지 확인하는 메서드
    public boolean isMySubDeptEmp(Emp emp, Emp targetEmp) {
        if (emp.getRole().equals(ROLE_ADMIN)) {
            return true;
        }
        if (!emp.getRole().equals(ROLE_MANAGER)) {
            return false;
        }
        List<Dept> subDeptList = deptMapper.getSubDeptList(emp.getDeptId());
        return subDeptList.stream()
                .anyMatch(d -> Objects.equals(d.getDeptId(), targetEmp.getDeptId()));
    }

    public boolean isHrDeptEmp(Emp emp) {
        return emp.getDeptId() == HR_DEPT_ID;
    }

    public String changePassword(String empId, String currentPassword, String newPassword) {
        String savedPassword = empMapper.getEmpById(empId).getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String message;
        if (!encoder.matches(currentPassword, savedPassword)) {
            message = "현재 비밀번호가 다릅니다.";
        } else if (empMapper.changePassword(empId, encoder.encode(newPassword)) != 1) {
            message = "비밀번호 변경 실패";
        } else {
            message = "비밀번호 변경이 완료되었습니다.";
        }
        return message;
    }

    public int resetPasswordByEmail(String empId, String newPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return empMapper.changePassword(empId, encoder.encode(newPassword));
    }

    public String findManager(String empId) {
        //일반 본인 부서의 부서장을 찾음
        String managerEmpId = empMapper.findDeptManager(empId);

        //본인 부서의 부서장이 본인이라면, 즉 내가 부서장이라면 상위 부서의 부서장을 찾음
        if (managerEmpId.equals(empId)) {
            managerEmpId = empMapper.findParentDeptManager(empId);
        }
        //대표 또는 admin의 경우 상위부서장이 없기 때문에 본인을 return
        if (managerEmpId == null) {
            return empId;
        }
        return managerEmpId;
    }
}
