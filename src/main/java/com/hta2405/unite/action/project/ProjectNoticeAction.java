package com.hta2405.unite.action.project;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ProjectDAO;
import com.hta2405.unite.dto.ProjectDetail;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ProjectNoticeAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String userid = (String) req.getSession().getAttribute("id");
        ProjectDAO projectDAO5 = new ProjectDAO();
        
        // 알림 데이터 가져오기
        List<ProjectDetail> projectDetail4 = projectDAO5.getNotice(userid);
        System.out.println("알림 : " + projectDetail4); // 값 확인용

        // JSON 형식으로 반환
        resp.setContentType("application/json; charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            String json = gson.toJson(projectDetail4); // 객체를 JSON 문자열로 변환
            out.print(json);
            out.flush();
        }

        // JSON 응답만 하고 JSP 포워딩은 필요 없음
        return null; 
    }
}
