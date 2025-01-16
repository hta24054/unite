package com.hta2405.unite.controller;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.dto.AttendInfoDTO;
import com.hta2405.unite.dto.VacationInfoDTO;
import com.hta2405.unite.service.AttendService;
import com.hta2405.unite.service.AuthService;
import com.hta2405.unite.service.EmpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/attend")
public class AttendController {
    private final EmpService empService;
    private final AttendService attendService;
    private final AuthService authService;

    @GetMapping("/info")
    public ModelAndView showEmpAttendPage(ModelAndView mv,
                                          @AuthenticationPrincipal UserDetails user,
                                          @RequestParam(required = false) String empId,
                                          @RequestParam(required = false) Integer year,
                                          @RequestParam(required = false) Integer month) {
        String targetEmpId = (ObjectUtils.isEmpty(empId)) ? user.getUsername() : empId;
        if (year == null || month == null) {
            year = LocalDate.now().getYear();
            month = LocalDate.now().getMonthValue();
            mv.setViewName("redirect:/attend/info?empId=" + targetEmpId + "&year=" + year + "&month=" + month);
            return mv;
        }

        Emp targetEmp = empService.getEmpById(targetEmpId);
        Emp emp = empService.getEmpById(user.getUsername());

        if (!authService.isAuthorizedToView(emp, targetEmp, user.getUsername().equals(targetEmpId))) {
            mv.setViewName("error/error");
            mv.addObject("errorMessage", "회원정보 열람 권한이 없습니다.");
            return mv;
        }

        AttendInfoDTO attendInfoDTO = attendService.getAttendInfoDTO(year, month, targetEmp);
        mv.addObject("attendInfoDTO", attendInfoDTO);
        mv.setViewName("attend/attendInfo");
        return mv;
    }

    @GetMapping("/vacation")
    public ModelAndView showVacationPage(ModelAndView mv,
                                         @AuthenticationPrincipal UserDetails user,
                                         @RequestParam(required = false) String empId,
                                         @RequestParam(required = false) Integer year) {
        String targetEmpId = (ObjectUtils.isEmpty(empId)) ? user.getUsername() : empId;
        if (year == null) {
            year = LocalDate.now().getYear();
            mv.setViewName("redirect:/attend/vacation?empId=" + targetEmpId + "&year=" + year);
            return mv;
        }

        Emp targetEmp = empService.getEmpById(targetEmpId);
        Emp emp = empService.getEmpById(user.getUsername());

        if (!authService.isAuthorizedToView(emp, targetEmp, user.getUsername().equals(targetEmpId))) {
            mv.setViewName("error/error");
            mv.addObject("errorMessage", "회원정보 열람 권한이 없습니다.");
            return mv;
        }

        VacationInfoDTO vacationInfoDTO = attendService.getVacationInfoDTO(year, targetEmp);
        mv.addObject("vacationInfoDTO", vacationInfoDTO);
        mv.setViewName("attend/vacationInfo");
        return mv;
    }
}
