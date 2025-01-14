package com.hta2405.unite.security;

import com.hta2405.unite.domain.Dept;
import io.jsonwebtoken.*;
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

    private final SecretKey SECRET_KEY;

    public JwtUtil(@Value("${spring.jwt.secret}") String secretKey) {
        this.SECRET_KEY = new SecretKeySpec(
                secretKey.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }

    public String generateToken(String username, String role, Long deptId) {
        log.info("generateToken username = {}", username);
        long EXPIRED_MS = 60 * 60 * 1000L;
        String generatedToken = Jwts.builder()
                .claim("empId", username)
                .claim("role", role)
                .claim("deptId", deptId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRED_MS))
                .signWith(SECRET_KEY)
                .compact();
        log.info("TOKEN = {}", generatedToken);
        return generatedToken;
    }

    public Claims validateToken(String token) {
        try {
            Claims claims = getJwtParser().parseSignedClaims(token).getPayload();
            if (claims.getExpiration().before(new Date())) {
                throw new JwtException("토큰 만료");
            }
            return claims;
        } catch (JwtException e) {
            log.error("토큰 오류: {}", e.getMessage());
            throw new JwtException("토큰 오류", e);
        }
    }

    public boolean isExpired(String token) {
        try {
            Claims claims = getJwtParser().parseSignedClaims(token).getPayload();
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid token", e);
        }
    }

    private JwtParser getJwtParser() {
        return Jwts.parser().verifyWith(SECRET_KEY).build();
    }
}