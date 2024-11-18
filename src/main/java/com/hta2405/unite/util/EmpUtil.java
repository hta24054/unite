package com.hta2405.unite.util;

import com.hta2405.unite.dao.DeptDao;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Emp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class EmpUtil {
    public static boolean changePassword(Emp emp, String newPassword) {
        emp.setPassword(EmpUtil.hashingPassword(newPassword));
        EmpDao empDao = new EmpDao();
        return empDao.updateEmp(emp) == 1;
    }

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

    public static String hashingPassword(String inputPassword) {
        String uuid = UUID.randomUUID().toString().substring(0, 5);
        return sha256(inputPassword + uuid) + uuid;
    }

    public static boolean verifyPassword(Emp emp, String inputPassword) {
        String dbPassword = emp.getPassword();
        String salt = dbPassword.substring(64);
        String savePass = dbPassword.substring(0, 64);//db에 저장되어 있는 해시 값
        String hashPass = sha256(inputPassword + salt);//입력받은 비밀번호 암호화한 해시 값
        return savePass.equals(hashPass);
    }

    public static String sha256(String input) {
        try {
            // SHA-256 알고리즘 인스턴스 생성
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // 입력 데이터를 바이트 배열로 변환 후 해시 계산
            byte[] encodedHash = digest.digest(input.getBytes());

            // 해시 결과를 16진수 문자열로 변환
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("sha256() 에러: " + e);
            return null;
        }
    }

    public static boolean isHrDept(Emp loginEmp) {
        String deptName = new DeptDao().getDeptNameById(loginEmp.getDeptId());
        return deptName.equals("인사관리팀") || deptName.equals("관리자");
    }
}
