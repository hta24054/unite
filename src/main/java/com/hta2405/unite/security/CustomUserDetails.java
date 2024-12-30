package com.hta2405.unite.security;

import com.hta2405.unite.domain.Emp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private final Emp emp;

    public CustomUserDetails(Emp emp) {
        this.emp = emp;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add((GrantedAuthority) emp::getRole); // 강제로 ROLE_ 접두사 추가
        return collection;
    }

    @Override
    public String getPassword() {
        return emp.getPassword();
    }

    @Override
    public String getUsername() {
        return emp.getEmpId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
