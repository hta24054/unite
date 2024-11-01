package com.hta2405.unite.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;
import com.hta2405.unite.dao.ProjectDAO;
import com.hta2405.unite.dto.ProjectInfo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//메인프로젝트
public class ProjectGetOngoingAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProjectDAO projectDAO = new ProjectDAO();
        List<ProjectInfo> ongoingProjects = projectDAO.getOngoingProjects();

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
