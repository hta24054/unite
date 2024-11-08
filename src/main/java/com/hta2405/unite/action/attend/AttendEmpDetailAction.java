package com.hta2405.unite.action.attend;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.util.AttendUtil;
import com.hta2405.unite.util.CommonUtil;
import com.hta2405.unite.util.EmpUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AttendEmpDetailAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EmpDao empDao = new EmpDao();

        Emp loginEmp = empDao.getEmpById((String) req.getSession().getAttribute("id"));
        Emp targetEmp = empDao.getEmpById(req.getParameter("emp"));

        //본인이 부서장인지 확인
        if (!EmpUtil.isManager(loginEmp)) {
            return CommonUtil.alertAndGoBack(resp, "부서장이 아닙니다.");
        }

        //해당 타겟 직원의 부서장인지 확인
        if (!EmpUtil.isValidToAccessEmp(loginEmp, targetEmp)) {
            return CommonUtil.alertAndGoBack(resp, "해당 직원을 확인할 권한이 없습니다.");
        }
        return new AttendUtil().getAttendDetail(req, targetEmp);
    }
}
