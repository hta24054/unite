package com.hta2405.unite.action;

import java.io.IOException;
import java.util.List;

import com.hta2405.unite.dao.ProjectDAO;
import com.hta2405.unite.dto.ProjectInfo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//프로젝트 생성하기 위한 Action. 
public class ProjectDoCreateAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String managerId = req.getParameter("manager");
        String projectName = req.getParameter("name");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String description = req.getParameter("description");
        String participants = req.getParameter("participants");
        String viewers = req.getParameter("viewers");

        ProjectDAO projectDAO = new ProjectDAO();

        // 프로젝트 생성 및 ID 반환
        int projectId = projectDAO.createProject(managerId, projectName, startDate, endDate, description);

        // 프로젝트 멤버 추가
        if (projectId > 0) {
            // 책임자 추가
            projectDAO.addProjectMember(projectId, managerId, "MANAGER");

            // 참여자 및 열람자 추가
            if (participants != null && !participants.isEmpty()) {
                projectDAO.addProjectMembers(projectId, participants, "PARTICIPANT");
            }
            if (viewers != null && !viewers.isEmpty()) {
                projectDAO.addProjectMembers(projectId, viewers, "VIEWER");
            }
            
            // 성공 메시지 설정
            req.setAttribute("message", "프로젝트가 생성되었습니다.");
        }else {
            // 실패 시 메시지 설정 (원하는 경우)
            req.setAttribute("message", "프로젝트 생성에 실패했습니다.");
        }

        // 진행 중인 프로젝트 가져오기
        List<ProjectInfo> ongoingProjects = projectDAO.getOngoingProjects();
        req.setAttribute("ongoingProjects", ongoingProjects); // 요청 속성에 추가

        // 포워드 설정
        ActionForward forward = new ActionForward();
        forward.setPath("/WEB-INF/views/project/project_main.jsp");
        forward.setRedirect(false);
        return forward;
    }

}
