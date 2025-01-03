package com.hta2405.unite.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hta2405.unite.dto.EmpAdminUpdateDTO;
import com.hta2405.unite.dto.EmpInfoDTO;
import com.hta2405.unite.dto.EmpSelfUpdateDTO;
import com.hta2405.unite.service.EmpService;
import com.hta2405.unite.service.ProfileImgService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/emp")
@Slf4j
public class EmpController {

    private final EmpService empService;
    private final ProfileImgService profileImgService;

    public EmpController(EmpService empService, ProfileImgService profileImgService) {
        this.empService = empService;
        this.profileImgService = profileImgService;
    }

    @GetMapping("/info")
    public ModelAndView showEmpInfoPage(ModelAndView mv, @RequestParam(required = false) String empId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginEmpId = authentication.getName();

        String targetEmpId = (empId == null || empId.isEmpty()) ? loginEmpId : empId;

        //관리자 -> 부서원 조회 아닐 시 거부하는 로직 구현 필요
        EmpInfoDTO empInfoDTO = empService.getEmpInfoDTO(targetEmpId);
        System.out.println(empInfoDTO.getJobList());
        System.out.println(empInfoDTO.getDeptList());
        mv.addObject("empInfoDTO", empInfoDTO);
        mv.setViewName("emp/empInfo");
        return mv;
    }

    @GetMapping("/editable-field")
    @ResponseBody
    public Map<String, Object> getEditableFields(@AuthenticationPrincipal UserDetails user) {
        Map<String, Object> response = new HashMap<>();
        List<String> field;

        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            field = Stream.of(EmpAdminUpdateDTO.class.getDeclaredFields())
                    .map(Field::getName)
                    .collect(Collectors.toList());
            response.put("role", "ROLE_ADMIN");
        } else {
            field = Stream.of(EmpSelfUpdateDTO.class.getDeclaredFields())
                    .map(Field::getName)
                    .collect(Collectors.toList());
            response.put("role", "ROLE_USER");
        }
        response.put("field", field);
        return response;
    }

    @PatchMapping("/{empId}")
    @ResponseBody
    public String updateEmpSelf(@PathVariable String empId,
                                @RequestPart(value = "file", required = false) MultipartFile file,
                                @RequestBody EmpSelfUpdateDTO dto,
                                @AuthenticationPrincipal UserDetails user) {
        //수정 대상 직원이 본인이 아닌 경우 인사정보 수정 제한
        if (!empId.equals(user.getUsername())) {
            return "인사정보 수정 실패";
        }

        int result = empService.updateEmpBySelf(empId, file, dto);
        if (result != 1) {
            return "인사정보 수정 실패";
        }

        return "인사정보 수정 성공";
    }

    @PatchMapping("/admin/{empId}")
    @ResponseBody
    public String updateEmpAdmin(@PathVariable String empId,
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
        int result = empService.updateEmpByAdmin(empId, file, dto);
        if (result != 1) {
            return "인사정보 수정 실패";
        }
        return "인사정보 수정 성공";
    }

    @GetMapping("/profile-image")
    @ResponseBody
    public void getProfileImage(String fileUUID, HttpServletResponse response) {
        profileImgService.getProfileImage(fileUUID, response);
    }
}
