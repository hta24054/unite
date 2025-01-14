package com.hta2405.unite.controller;

import com.hta2405.unite.domain.Notice;
import com.hta2405.unite.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Slf4j
public class HomeController {
    private final NoticeService noticeService;

    public HomeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        model.addAttribute("name", name);
        return "home";
    }

    @GetMapping("/home/notice")
    @ResponseBody
    public List<Notice> getAliveNotice() {
        return noticeService.getAliveNotice();
    }
}
