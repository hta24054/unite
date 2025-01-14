package com.hta2405.unite.security;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class ManagerAndHRAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext object) {
        Authentication authentication = authenticationSupplier.get();

        // 인증되지 않은 사용자 처리
        if (authentication == null || !authentication.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }

        // 권한 검증: ROLE_ADMIN 또는 ROLE_MANAGER
        boolean hasRole = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN") || role.equals("ROLE_MANAGER"));

        // 추가 조건: deptId = 인사부서 id
        Long deptId = null;
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            deptId = ((CustomUserDetails) authentication.getPrincipal()).getDeptId();
        }

        if (deptId == null) {
            return new AuthorizationDecision(false);
        }

        // 최종 접근 결정
        boolean hasAccess = hasRole || 1120L == deptId;

        return new AuthorizationDecision(hasAccess);
    }
}