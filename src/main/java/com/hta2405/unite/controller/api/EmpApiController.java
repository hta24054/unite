package com.hta2405.unite.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.dto.EmpAdminUpdateDTO;
import com.hta2405.unite.dto.EmpListDTO;
import com.hta2405.unite.dto.EmpSelfUpdateDTO;
import com.hta2405.unite.enums.Role;
import com.hta2405.unite.security.CustomUserDetails;
import com.hta2405.unite.service.EmpService;
import com.hta2405.unite.service.ProfileImgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/emp")
public class EmpApiController {
    private final EmpService empService;
    private final ProfileImgService profileImgService;

    @GetMapping("/editable-field")
    public Map<String, Object> getEditableFields(@AuthenticationPrincipal CustomUserDetails user) {
        Map<String, Object> response = new HashMap<>();
        List<String> field;

        if (user.getAuthorities().contains(new SimpleGrantedAuthority(Role.ROLE_ADMIN.getRoleName()))) {
            field = Stream.of(EmpAdminUpdateDTO.class.getDeclaredFields())
                    .map(Field::getName)
                    .collect(Collectors.toList());
            response.put("role", Role.ROLE_ADMIN.getRoleName());
        } else {
            field = Stream.of(EmpSelfUpdateDTO.class.getDeclaredFields())
                    .map(Field::getName)
                    .collect(Collectors.toList());
            response.put("role", Role.ROLE_MEMBER.getRoleName());
        }
        response.put("field", field);
        return response;
    }

    @PatchMapping("/{empId}")
    public int updateEmpSelf(@PathVariable String empId,
                             @RequestPart(value = "file", required = false) MultipartFile file,
                             @RequestPart(value = "dto") String dtoJson,
                             @AuthenticationPrincipal UserDetails user) {
        //수정 대상 직원이 본인이 아닌 경우 인사정보 수정 제한
        if (!empId.equals(user.getUsername())) {
            return 0;
        }
        // JSON 데이터를 DTO로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        EmpSelfUpdateDTO dto;
        try {
            dto = objectMapper.readValue(dtoJson, EmpSelfUpdateDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 데이터 변환 실패", e);
        }

        return empService.updateEmpBySelf(empId, file, dto);
    }

    @PatchMapping("/admin/{empId}")
    public int updateEmpAdmin(@PathVariable String empId,
                              @RequestPart(value = "file", required = false) MultipartFile file,
                              @RequestPart(value = "dto") String dtoJson
    ) {
        // JSON 데이터를 DTO로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        EmpAdminUpdateDTO dto;
        try {
            dto = objectMapper.readValue(dtoJson, EmpAdminUpdateDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 데이터 변환 실패", e);
        }
        return empService.updateEmpByAdmin(empId, file, dto);
    }

    @PostMapping("/password")
    public String changePassword(@AuthenticationPrincipal UserDetails user,
                                 String currentPassword,
                                 String newPassword) {
        return empService.changePassword(user.getUsername(), currentPassword, newPassword);
    }

    @GetMapping("/empTree")
    public List<EmpListDTO> getEmpListByDeptId(Long deptId) {
        return empService.getEmpListByDeptId(deptId);
    }

    @GetMapping("/empTree-search")
    public List<EmpListDTO> getEmpListByName(String query) {
        query = "%" + query + "%";
        return empService.getEmpListByName(query);
    }

    @GetMapping("/profile-image")
    public ResponseEntity<Resource> getProfileImage(String empId) {
        Emp emp = empService.getEmpById(empId);
        return profileImgService.getProfileImage(emp);
    }
}
