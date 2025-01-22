package com.hta2405.unite.controller;

import com.hta2405.unite.domain.Dept;
import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.dto.EmpListDTO;
import com.hta2405.unite.enums.Role;
import com.hta2405.unite.service.DeptService;
import com.hta2405.unite.service.EmpService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/manager")
@Slf4j
@AllArgsConstructor
public class ManagerController {
    private final EmpService empService;
    private final DeptService deptService;


    @GetMapping("/empInfoList")
    public ModelAndView showEmpInfoList(@AuthenticationPrincipal UserDetails user, ModelAndView mv) {
        List<Dept> deptList = getAvailableDeptList(user);
        List<EmpListDTO> empList = empService.getEmpListDTOByDeptList(deptList);
        String message = "부서 인사정보 조회 페이지입니다. 이름을 클릭하면 인사정보 조회가 가능합니다.";
        return empService.showEmpList(mv, "부서 인사정보", message, empList, "/manager/empInfo");
    }

    @GetMapping("/attendInfoList")
    public ModelAndView showAttendInfoList(@AuthenticationPrincipal UserDetails user, ModelAndView mv) {
        List<Dept> deptList = getAvailableDeptList(user);
        List<EmpListDTO> empList = empService.getEmpListDTOByDeptList(deptList);
        String message = "근태정보 조회 페이지입니다. 이름을 클릭하면 근태정보 조회가 가능합니다.";
        return empService.showEmpList(mv, "부서 근태현황", message, empList, "/manager/attendInfo");
    }

    @GetMapping("/vacationInfoList")
    public ModelAndView showVacationInfoList(@AuthenticationPrincipal UserDetails user, ModelAndView mv) {
        List<Dept> deptList = getAvailableDeptList(user);
        List<EmpListDTO> empList = empService.getEmpListDTOByDeptList(deptList);
        String message = "휴가정보 조회 페이지입니다. 이름을 클릭하면 휴가정보 조회가 가능합니다.";
        return empService.showEmpList(mv, "부서 휴가현황", message, empList, "/manager/vacationInfo");
    }

    private List<Dept> getAvailableDeptList(UserDetails user) {
        Emp emp = empService.getEmpById(user.getUsername());
        List<Dept> deptList;
        if (empService.isHrDeptEmp(emp) || emp.getRole() == Role.ROLE_ADMIN) {
            deptList = deptService.getAllDept();
        } else {
            deptList = deptService.getSubDeptList(emp.getDeptId());
        }
        return deptList;
    }

    @GetMapping("/empInfo")
    public String showEmpInfo(String empId) {
        return "redirect:/emp/info?empId=" + empId;
    }

    @GetMapping("/attendInfo")
    public String showAttendInfo(String empId) {
        return "redirect:/attend/info?empId=" + empId;
    }

    @GetMapping("/vacationInfo")
    public String showVacationInfo(String empId) {
        return "redirect:/attend/vacation?empId=" + empId;
    }
}
