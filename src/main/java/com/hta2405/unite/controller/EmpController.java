package com.hta2405.unite.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.dto.EmpAdminUpdateDTO;
import com.hta2405.unite.dto.EmpInfoDTO;
import com.hta2405.unite.dto.EmpListDTO;
import com.hta2405.unite.dto.EmpSelfUpdateDTO;
import com.hta2405.unite.enums.Role;
import com.hta2405.unite.security.CustomUserDetails;
import com.hta2405.unite.service.AuthService;
import com.hta2405.unite.service.EmpService;
import com.hta2405.unite.service.ProfileImgService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
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
@AllArgsConstructor
public class EmpController {
    private final EmpService empService;
    private final AuthService authService;

    @GetMapping("/info")
    public ModelAndView showEmpInfoPage(ModelAndView mv,
                                        @AuthenticationPrincipal UserDetails user,
                                        @RequestParam(required = false) String empId) {
        String targetEmpId = (ObjectUtils.isEmpty(empId)) ? user.getUsername() : empId;
        Emp targetEmp = empService.getEmpById(targetEmpId);
        Emp emp = empService.getEmpById(user.getUsername());

        //열람 권한 조회
        if (!authService.isAuthorizedToView(emp, targetEmp, user.getUsername().equals(targetEmpId))) {
            mv.setViewName("error/error");
            mv.addObject("errorMessage", "회원정보 열람 권한이 없습니다.");
            return mv;
        }

        EmpInfoDTO empInfoDTO = empService.getEmpInfoDTO(targetEmpId);
        mv.addObject("empInfoDTO", empInfoDTO);
        mv.setViewName("emp/empInfo");

        return mv;
    }

    @GetMapping("/password")
    public String showChangePassword() {
        return "emp/changePassword";
    }
}
