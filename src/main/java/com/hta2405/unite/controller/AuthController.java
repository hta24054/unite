package com.hta2405.unite.controller;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.dto.LoginDTO;
import com.hta2405.unite.security.JwtUtil;
import com.hta2405.unite.service.EmpService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@Slf4j
@RequestMapping("/auth")
public class AuthController {
    private final JwtUtil jwtUtil;
    private final EmpService empService;

    public AuthController(JwtUtil jwtUtil, EmpService empService) {
        this.jwtUtil = jwtUtil;
        this.empService = empService;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/loginForm";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        Emp emp = empService.getEmpById(loginDTO.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("user not found"));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!emp.getEmpId().equals(loginDTO.getUsername()) || !encoder.matches(loginDTO.getPassword(), emp.getPassword())) {
            return ResponseEntity.status(401).body("로그인 실패");
        }

        String token = jwtUtil.generateToken(emp.getEmpId(), emp.getRole());

        Cookie jwtCookie = new Cookie("jwtToken", token);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(60 * 60 * 24); // 1일
        response.addCookie(jwtCookie);
        return ResponseEntity.ok("로그인 성공");
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
}