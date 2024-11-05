package com.hta2405.unite.action;

import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Emp;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

import static com.hta2405.unite.util.EmpUtil.sha256;
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

        ActionForward forward = new ActionForward();

        if (emp != null) {
            String dbPassword = emp.getPassword();
            System.out.println("db에 저장된 pass 값은 " + dbPassword);
            String salt = dbPassword.substring(64);
            System.out.println("salt 값은 " + salt);
            String savePass = dbPassword.substring(0, 64);//db에 저장되어 있는 해시 값
            System.out.println("저장된 해쉬 값은 " + savePass);

            if (verifyPassword(emp, pass)) {//로그인 성공
                HttpSession session = req.getSession();
                session.setAttribute("id", id);

                String IDStore = req.getParameter("remember");
                Cookie cookie = new Cookie("id", id);

                // ID 기억하기를 체크한 경우
                if (IDStore != null && IDStore.equals("store")) {
                    cookie.setMaxAge(10 * 60);//쿠키의 유효시간을 10분으로 설정
                    //클라이언트로 쿠키값을 전송합니다.
                    System.out.println("쿠키확인");
                } else {
                    cookie.setMaxAge(0);
                }
                resp.addCookie(cookie);
                forward.setRedirect(true);
                forward.setPath("../emp/home");
                return forward;
            } else {
                message = "비밀번호가 일치하지 않습니다.";
            }
        } else {
            message = "아이디가 존재하지 않습니다.";
        }

        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.print("<script>");
        out.print("alert('" + message + "');");
        out.print("history.back();");
        out.print("</script>");
        out.close();
        return null;

    }
}
