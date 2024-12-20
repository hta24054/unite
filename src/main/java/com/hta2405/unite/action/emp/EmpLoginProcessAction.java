package com.hta2405.unite.action.emp;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.util.CommonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static com.hta2405.unite.util.EmpUtil.verifyPassword;

public class EmpLoginProcessAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String id = req.getParameter("id");
        String pass = req.getParameter("pass");
        String message;

        EmpDao dao = new EmpDao();
        Emp emp = dao.getEmpById(id);

        if (emp != null) {
            if (verifyPassword(emp, pass)) {//로그인 성공
                HttpSession session = req.getSession();
                session.setAttribute("id", id);
                session.setAttribute("ename", emp.getEname());
                session.setAttribute("profileUUID", emp.getImgUUID());
                String IDStore = req.getParameter("remember");
                Cookie cookie = new Cookie("id", id);

                // ID 기억하기를 체크한 경우
                if (IDStore != null && IDStore.equals("store")) {
                    cookie.setMaxAge(10 * 60);//쿠키의 유효시간을 10분으로 설정
                    //클라이언트로 쿠키값을 전송합니다.
                } else {
                    cookie.setMaxAge(0);
                }
                resp.addCookie(cookie);
                return new ActionForward(true, req.getContextPath() + "/home");
            } else {
                message = "비밀번호가 일치하지 않습니다.";
            }
        } else {
            message = "아이디가 존재하지 않습니다.";
        }
        return CommonUtil.alertAndGoBack(resp, message);//요청한 곳으로 돌아감
    }
}
