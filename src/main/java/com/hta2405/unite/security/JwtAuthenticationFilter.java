package com.hta2405.unite.security;

import com.google.gson.Gson;
import com.hta2405.unite.dto.LoginDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(String loginUrl, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        super(loginUrl);
        this.jwtUtil = jwtUtil;
        setAuthenticationManager(authenticationManager);
        log.debug("AuthenticationManager: {}", authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {
        Gson gson = new Gson();
        LoginDTO loginDTO = gson.fromJson(request.getReader(), LoginDTO.class);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getEmpId(), loginDTO.getPassword());

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_MEMBER");
        Long deptId = userDetails.getDeptId();

        String token = jwtUtil.generateToken(username, role, deptId);

        // JWT를 쿠키에 저장
        Cookie jwtCookie = new Cookie("jwtToken", token);
        jwtCookie.setPath("/"); // 모든 경로에서 쿠키 사용 가능
        jwtCookie.setHttpOnly(true); // JavaScript에서 접근 불가 (보안 강화)
        jwtCookie.setMaxAge(60 * 60 * 24); // 쿠키 유효 기간: 1일
        response.addCookie(jwtCookie);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.format("{\"token\": \"%s\"}", token));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed: " + failed.getMessage());
    }
}