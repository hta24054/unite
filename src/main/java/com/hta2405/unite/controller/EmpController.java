package com.hta2405.unite.controller;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.dto.EmpInfoDTO;
import com.hta2405.unite.service.AuthService;
import com.hta2405.unite.service.EmpService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
