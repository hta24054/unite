package com.hta2405.unite.controller.api;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.service.EmailService;
import com.hta2405.unite.service.EmpService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthApiController {
    private final EmailService emailService;
    private final EmpService empService;

    @PostMapping("/sendAuthCode")
    public boolean sendAuthCode(String ename, String email, HttpSession session) {
        String empId = session.getAttribute("pwEmpId").toString();
        Emp emp = empService.getEmpById(empId);
        if (!emp.getEname().equals(ename) || !emp.getEmail().equals(email)) {
            log.info("패스워드 찾기 {} - 사용자 정보 다름", emp.getEmpId());
            return false;
        }
        String authCode = emailService.sendAuthCode(email);
        session.setAttribute("authCode", authCode);
        session.setAttribute("authCodeGeneratedAt", LocalDateTime.now());
        return true;
    }

    @PostMapping("/verifyAuthCode")
    public boolean verifyAuthCode(String inputCode, HttpSession session) {
        String storedCode = (String) session.getAttribute("authCode");
        LocalDateTime authCodeGeneratedAt = LocalDateTime.parse(session.getAttribute("authCodeGeneratedAt").toString());
        //제한시간 5분
        if (storedCode == null
                || !storedCode.equals(inputCode)
                || authCodeGeneratedAt.plusMinutes(5).isBefore(LocalDateTime.now())) {
            log.info("인증코드 검증 실패");
            return false;
        }
        log.info("인증코드 검증 성공");
        //검증 성공 후 세션에서 삭제
        session.removeAttribute("authCode");
        session.setAttribute("passwordResetAllowed", true);
        return true;
    }

    @PostMapping("/resetPassword")
    public int changePassword(String newPassword, HttpSession session) {
        String empId = session.getAttribute("pwEmpId").toString();
        session.removeAttribute("targetEmpId"); //세션정보 삭제
        session.removeAttribute("authCode"); //세션정보 삭제
        return empService.resetPasswordByEmail(empId, newPassword);
    }
}