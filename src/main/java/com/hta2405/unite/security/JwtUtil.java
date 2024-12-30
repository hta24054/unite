package com.hta2405.unite.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
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

    private final SecretKey SECRET_KEY;
    private final Long EXPIRED_MS = 60 * 60 * 1000L; //1시간

    public JwtUtil(@Value("${spring.jwt.secret}") String secretKey) {
        this.SECRET_KEY = new SecretKeySpec(
                secretKey.getBytes(StandardCharsets.UTF_8),
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

    private JwtParser getJwtParser() {
        return Jwts.parser().verifyWith(SECRET_KEY).build();
    }
}