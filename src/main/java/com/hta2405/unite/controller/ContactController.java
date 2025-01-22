package com.hta2405.unite.controller;

import com.hta2405.unite.dto.EmpListDTO;
import com.hta2405.unite.service.DeptService;
import com.hta2405.unite.service.EmpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/contact")
@RequiredArgsConstructor
@Slf4j
public class ContactController {

    private final EmpService empService;
    private final DeptService deptService;

    @GetMapping("/inner")
    public ModelAndView showInnerContact(ModelAndView mv) {
        List<EmpListDTO> empList = empService.getEmpListDTOByDeptList(deptService.getAllDept());
        String message = "사내 주소록입니다.";
        mv.addObject("title", "사내 주소록");
        mv.addObject("message", message);
        mv.addObject("empList", empList);
        mv.setViewName("/contact/innerContact");
        return mv;
    }

    @GetMapping("/outer")
    public String showOuterContact() {
        return "/contact/outerContact";
    }
}
