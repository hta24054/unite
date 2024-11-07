package com.hta2405.unite.action;

import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.util.CommonUtil;
import com.hta2405.unite.util.EmpUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class MyPagePasswordChangeProcessAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String currentPassword = req.getParameter("currentPassword");
        String newPassword = req.getParameter("newPassword");
        EmpDao empDao = new EmpDao();
        Emp emp = empDao.getEmpById((String) req.getSession().getAttribute("id"));

        String message = "비밀번호 변경이 완료되었습니다.";

        if (!EmpUtil.verifyPassword(emp, currentPassword)) {
            message = "비밀번호 변경 실패";
        }

        emp.setPassword(EmpUtil.hashingPassword(newPassword));

        if (empDao.updateEmp(emp) != 1) {
            message = "비밀번호 변경 실패";
        }

        return CommonUtil.alertAndGoBack(resp, message);
    }
}
