package com.hta2405.unite.service;

import com.hta2405.unite.domain.Emp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final EmpService empService;

    /**
     * 인사정보, 근태정보, 휴가정보 조회 권한을 확인하는 메서드
     * @param emp 사용자
     * @param targetEmp 조회대상자
     * @param isSelf 사용자 = 조회대상자인지 확인
     * @return 조회 가능 여부
     */
    public boolean isAuthorizedToView(Emp emp, Emp targetEmp, boolean isSelf) {
        /**
         * 전체 경우의 수
         * 1. 본인 정보 = 누구나 열람 가능
         * 2. 본인이 아닌 정보
         *  2-1. 관리자("ROLE_ADMIN") 열람가능
         *  2-2. HR부서(empService.isHrDeptEmp(emp) == true) 열람 가능
         *  2-3. 부서장("ROLE_ADMIN") + 내 부하직원인지 확인(empService.isMySubDeptEmp(emp, targetEmp) = true) 열람가능
         */
        if (isSelf) {
            return true; // 본인 정보는 누구나 열람 가능
        }

        String role = emp.getRole();

        // 관리자 권한은 열람 가능
        if ("ROLE_ADMIN".equals(role)) {
            return true;
        }

        // HR 부서는 열람 가능
        if (empService.isHrDeptEmp(emp)) {
            return true;
        }

        // 부서장 권한 + 부하직원 확인
        return "ROLE_MANAGER".equals(role) && empService.isMySubDeptEmp(emp, targetEmp);
    }
}
