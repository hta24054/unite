package com.hta2405.unite.controller;

import com.hta2405.unite.dto.EmpInfoDTO;
import com.hta2405.unite.dto.EmpSelfUpdateDTO;
import com.hta2405.unite.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/emp")
@Slf4j
public class EmpController {
    private final EmpService empService;

    public EmpController(EmpService empService) {
        this.empService = empService;
    }

    @GetMapping("/info")
    public ModelAndView showEmpInfoPage(ModelAndView mv, @RequestParam(required = false) String empId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginEmpId = authentication.getName();

        String targetEmpId = (empId == null || empId.isEmpty()) ? loginEmpId : empId;
        EmpInfoDTO empInfoDTO = empService.getEmpInfoDTO(targetEmpId);
        mv.addObject("empInfoDTO", empInfoDTO);
        mv.setViewName("/emp/empInfo");
        return mv;
    }

    @PatchMapping("/{empId}")
    @ResponseBody
    public String updateEmployee(@PathVariable String empId, EmpSelfUpdateDTO dto) {
        int result = empService.updateEmp(empId, dto);
        if (result != 1) {
            return "인사정보 수정 실패";
        }
        return "인사정보 수정 성공";
    }
}
