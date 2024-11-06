package com.hta2405.unite.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dao.ProjectDAO;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.dto.ProjectInfo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//메인프로젝트
public class ProjectGetOngoingAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 로그인된 사용자 ID 가져오기 (세션에서)
        //Integer loggedInUserId = (Integer) req.getSession().getAttribute("userId");  // "userId"는 세션에 저장된 사용자 ID 키입니다.

    	String userid = (String) req.getSession().getAttribute("id");

        ProjectDAO projectDAO = new ProjectDAO();
        List<ProjectInfo> ongoingProjects = projectDAO.getOngoingProjects(userid);  // 로그인된 사용자 ID 전달

        // JSON 형태로 데이터 변환
        Gson gson = new Gson();
        String json = gson.toJson(ongoingProjects);
        // 응답 설정
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();

        // ActionForward는 필요 없으므로 null 반환
        return null;
    }
}
