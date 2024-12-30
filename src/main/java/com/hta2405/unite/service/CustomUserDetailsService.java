package com.hta2405.unite.service;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.mybatis.mapper.EmpMapper;
import com.hta2405.unite.security.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final EmpMapper dao;

    public CustomUserDetailsService(EmpMapper dao) {
        this.dao = dao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Emp emp = dao.getEmpById(username);
        log.info("{}",emp);
        return new CustomUserDetails(emp);
    }
}