package com.hta2405.unite.controller;

import com.hta2405.unite.service.EmpService;
import com.hta2405.unite.service.HolidayService;
import com.hta2405.unite.service.NoticeService;
import com.hta2405.unite.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final NoticeService noticeService;
    private final EmpService empService;

    @GetMapping("/holiday")
    public String showHolidayPage() {
        return "/admin/holiday";
    }


    @GetMapping("/resource")
    public String showResourcePage() {
        return "/admin/resource";
    }


    @GetMapping("/notice")
    public ModelAndView showNoticePage(ModelAndView mv) {
        mv.addObject("noticeList", noticeService.getAllNotice());
        mv.setViewName("/admin/notice");
        return mv;
    }

    @GetMapping("/emp-manage")
    public String showEmpManage() {
        return "/admin/empManage";
    }

    @GetMapping("/emp-manage/info")
    public String showEmpInfo(String empId) {
        return "redirect:/emp/info?empId=" + empId;
    }

    @GetMapping("/emp-manage/register")
    public ModelAndView showEmpRegister(ModelAndView mv) {
        mv.setViewName("/emp/empRegister");
        mv.addObject("registerDTO", empService.getRegisterPageData());
        return mv;
    }
}
