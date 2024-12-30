package com.hta2405.unite.security;

import com.hta2405.unite.service.CustomUserDetailsService;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long expiredMs = 60 * 60 * 1000L;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }

    // JWT 생성
    public String generateToken(String username, String role) {
        log.info("generateToken username = {}", username);
        String generatedToken = Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
        log.info("TOKEN = {}", generatedToken);
        return generatedToken;
    }

    // 토큰에서 사용자 이름 추출
    public String getUsernameFromToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().get("username", String.class);
    }

    //토큰에서 권한 추출
    public String getRoleFromToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
                .getPayload().getExpiration().before(new Date());
    }
}