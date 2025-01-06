package com.hta2405.unite.controller;

import com.hta2405.unite.domain.Dept;
import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.dto.EmpListDTO;
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
        Emp emp = empService.getEmpById(user.getUsername());
        List<Dept> deptList;
        if (empService.isHrDeptEmp(emp)) {
            deptList = deptService.getAllDept();
        } else {
            deptList = deptService.getSubDeptList(emp.getDeptId());
        }
        List<EmpListDTO> empList = empService.getEmpListDTO(deptList);
        String message = "부서 인사정보 조회 페이지입니다. 이름을 클릭하면 인사정보 조회가 가능합니다.";
        return empService.showEmpList(mv, "부서 인사정보", message, empList, "/manager/empInfo");
    }

    @GetMapping("/empInfo")
    public String showEmpInfo(String empId) {
        return "redirect:/emp/info?empId=" + empId;
    }
}
