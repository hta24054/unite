package com.hta2405.unite.action.project;

import java.io.IOException;
import java.util.List;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dao.ProjectbDao;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.util.EmpUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ProjectDeleteAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 요청 파라미터 처리
        String userid = (String) req.getSession().getAttribute("id");
        int projectid = (Integer) req.getSession().getAttribute("projectId");
        int task_num = (Integer) req.getSession().getAttribute("taskNum");
        String pass = req.getParameter("board_pass");
        
        System.out.println("userid : " + userid);
        System.out.println("projectid : " + projectid);
        System.out.println("task_num : " + task_num);
        System.out.println("pass : " + pass);
        
        req.getSession().setAttribute("memberId", userid);
        
        // 직원 정보 조회
        EmpDao dao = new EmpDao();
        Emp emp = dao.getEmpById(userid);

        // 비밀번호 확인 후 삭제
        ProjectbDao del = new ProjectbDao();
        boolean deleteSuccess = false;
        ActionForward forward = new ActionForward();
        
        if (EmpUtil.verifyPassword(emp, pass)) {
            // 비밀번호가 맞으면 삭제 수행
            deleteSuccess = del.boardDelete(task_num);
            if (deleteSuccess) {
                System.out.println("게시물 삭제 성공");
                forward.setRedirect(true);
                // 파라미터 없이 'list' 페이지로 리다이렉트
                req.setAttribute("message", "게시물이 성공적으로 삭제되었습니다.");
                forward.setPath("list?memberId=" + userid); // URL에 파라미터 없이 'list'로 리다이렉트
            } else {
                System.out.println("게시물 삭제 실패");
                req.setAttribute("error", "게시물 삭제에 실패했습니다.");
                forward.setPath("list"); // URL에 파라미터 없이 'list'로 리다이렉트
            }
        } else {
            // 비밀번호가 틀린 경우
            System.out.println("비밀번호 확인 실패");
            req.setAttribute("error", "비밀번호가 일치하지 않습니다.");
            forward.setRedirect(true);
            // 파라미터 없이 'comm' 페이지로 리다이렉트
            forward.setPath("comm"); // URL에 파라미터 없이 'comm'으로 리다이렉트
        }
        
        return forward;
    }

}
