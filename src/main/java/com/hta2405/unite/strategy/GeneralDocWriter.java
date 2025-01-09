package com.hta2405.unite.strategy;

import com.hta2405.unite.domain.Dept;
import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.service.DeptService;
import com.hta2405.unite.service.EmpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class GeneralDocWriter implements DocWriter {
    private final EmpService empService;
    private final DeptService deptService;

    @Override
    public DocType getType() {
        return DocType.GENERAL;
    }

    @Override
    public void prepareWriter(String empId, Model model) {

        Emp emp = empService.getEmpById(empId);
        Dept dept = deptService.getDeptByEmpId(emp.getEmpId());

        model.addAttribute("empId", empId);
        model.addAttribute("ename", emp.getEname());
        model.addAttribute("deptName", dept.getDeptName());
        model.addAttribute("date", LocalDate.now());
    }

    @Override
    public String getView() {
        return "/doc/general_write";
    }
}
