package com.hta2405.unite.controller;

import com.hta2405.unite.mybatis.mapper.EmpMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final EmpMapper empMapper;

    @GetMapping("/login")
    public String login() {
        return "auth/loginForm";
    }


    @GetMapping("/logout")
    public void logout(HttpServletResponse response) throws IOException {
        Cookie jwtCookie = new Cookie("jwtToken", null);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);
        response.sendRedirect("/auth/login");
    }

    @GetMapping("/findPassword")
    public String showFindPasswordPage() {
        return "/auth/findPassword";
    }

    @PostMapping("/findPassword/verifyUser")
    public String showVerifyUserForm(String empId, HttpSession session) {
        session.setAttribute("pwEmpId", empId);
        return "/auth/verifyUser";
    }

    @GetMapping("/resetPassword")
    public String showChangePasswordPage(HttpSession session, Model model) {
        model.addAttribute("empId", session.getAttribute("pwEmpId"));
        return "/auth/resetPassword";
    }
}