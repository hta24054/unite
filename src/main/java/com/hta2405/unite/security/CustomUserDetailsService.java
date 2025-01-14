package com.hta2405.unite.security;


import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.service.EmpService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final EmpService empService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Emp emp = empService.getEmpById(username);
        if (emp == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new CustomUserDetails(
                emp.getEmpId(),
                emp.getPassword(),
                List.of(emp::getRole));
    }
}