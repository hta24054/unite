package com.hta2405.unite.util;

import com.hta2405.unite.dao.DeptDao;
import com.hta2405.unite.dto.Emp;

public class EmpUtil {

    public static boolean isManager(Emp emp) {
        if (emp.getEmpId().contains("admin")) {
            return true;
        }
        String managerId = new DeptDao().getManagerIdByDeptId(emp.getDeptId()); //내 부서의 부서장을 확인함
        return emp.getEmpId().equals(managerId);
    }

    public static boolean isValidToAccessEmp(Emp loginEmp, Emp targetEmp) {
        if (loginEmp.getEmpId().contains("admin")) {
            return true;
        }
        String deptToString = loginEmp.getDeptId().toString();
        int deep = loginEmp.getDeptId().toString().length();
        for (int i = 0; i < deptToString.length(); i++) {
            if (deptToString.charAt(i) != '0') {
                deep--;
            } else {
                break;
            }
        }
        return loginEmp.getDeptId() == Math.floor(targetEmp.getDeptId() / Math.pow(10, deep)) * Math.pow(10, deep);
    }
}
