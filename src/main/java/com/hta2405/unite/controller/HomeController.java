package com.hta2405.unite.controller;

import com.hta2405.unite.mybatis.mapper.JobMapper;
import com.hta2405.unite.service.DocService;
import com.hta2405.unite.service.EmpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    private final EmpService empService;
    private final DocService docService;
    private final JobMapper jobMapper;

    @GetMapping("/")
    public String index() {
        return "redirect:home";
    }

    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal UserDetails user) {
        model.addAttribute("name", empService.getEmpById(user.getUsername()).getEname());
        model.addAttribute("email", empService.getEmpById(user.getUsername()).getEmail());
        model.addAttribute("job", jobMapper.getJobByEmpId(user.getUsername()).getJobName());
        model.addAttribute("waiting", docService.getWaitingDocs(user.getUsername()).size());
        model.addAttribute("inProgress", docService.getInProgressDTO(user.getUsername()).size());
        return "home";
    }
}
