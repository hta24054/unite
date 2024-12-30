package com.hta2405.unite.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JwtUtil jwtUtil) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        // 세션 관리 비활성화 (JWT 사용 시 Stateless)
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 권한 및 인증 요청 설정
//        http.authorizeHttpRequests(
//                au -> au.requestMatchers("/login", "/css/**", "/js/**", "/image/**").permitAll()
//                        .requestMatchers("/home").hasAnyRole("ADMIN", "MEMBER")
//                        .anyRequest().authenticated()
//        );

        //개발용 임시 권한 모두 부여
                http.authorizeHttpRequests(
                au -> au.anyRequest().permitAll()
        );

        // JWT 필터 추가
        http.addFilterBefore(new JwtFilter(jwtUtil), JwtAuthenticationFilter.class)
                .addFilterAt(new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

//        http.exceptionHandling(exception -> exception
//                .authenticationEntryPoint((request, response, authException) -> {
//                    response.sendRedirect("/login");
//                })
//        );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
