package com.hta2405.unite.action.project;

import java.io.IOException;
import java.io.PrintWriter;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ProjectDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//프로젝트 상세. main에서 누르고 들어오는 첫 창
public class ProjectUpdateTaskDesignAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int projectId = Integer.parseInt(req.getParameter("projectId"));
        String memberId = req.getParameter("memberId");
        String memberRole = req.getParameter("memberRole");
        String taskContent = req.getParameter("taskContent");

        boolean success = false;
        if ("manager".equalsIgnoreCase(memberRole)) {
            ProjectDAO projectDAO = new ProjectDAO();
            success = projectDAO.updateTaskContent(projectId, memberId, taskContent);
        }

        StringBuilder jsonResponse = new StringBuilder();
        jsonResponse.append("{");
        jsonResponse.append("\"success\":").append(success);
        jsonResponse.append("}");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();
        out.print(jsonResponse.toString());
        out.flush();

        return null;
    }

}
