package com.hta2405.unite.action.project;

import java.io.IOException;
import java.util.List;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ProjectDAO;
import com.hta2405.unite.dto.ProjectInfo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//프로젝트 생성하기 위한 Action. 
public class ProjectDoCreateAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String managerEname = req.getParameter("manager"); // manager의 이름(enam)을 받음
        String projectName = req.getParameter("name");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String description = req.getParameter("description");
        String participants = req.getParameter("participants");
        String viewers = req.getParameter("viewers");

        ProjectDAO projectDAO = new ProjectDAO();

        // 매니저의 emp_id 가져오기
        String managerId = projectDAO.getEmpIdByEname(managerEname);

        // 프로젝트 생성 및 ID 반환
        int projectId = projectDAO.createProject(managerId, projectName, startDate, endDate, description);
        String userid = (String) req.getSession().getAttribute("id");

        // 프로젝트 멤버 추가
        if (projectId > 0) {
            // 매니저 추가
            projectDAO.addProjectMember(projectId, managerId, "MANAGER");
            // 작업 추가 (매니저)
            projectDAO.createTask(projectId, managerId);

            // 로그인된 사용자 ID 가져오기
            //Integer loggedInUserId = (Integer) req.getSession().getAttribute("userId"); // 세션에서 사용자 ID 가져오기
            // 참여자 추가
            if (participants != null && !participants.isEmpty()) {
                String[] participantArray = participants.split(",");
                for (String participant : participantArray) {
                    String empId = projectDAO.getEmpIdByEname(participant.trim());
                    if (empId != null && (userid.equals(empId) || !empId.equals(managerId))) {  // 본인이거나 다른 참여자일 경우만 추가
                        projectDAO.addProjectMember(projectId, empId, "PARTICIPANT");
                        // 작업 추가 (참여자)
                        projectDAO.createTask(projectId, empId);
                    }
                }
            }

            // 열람자 추가
            if (viewers != null && !viewers.isEmpty()) {
                String[] viewerArray = viewers.split(",");
                for (String viewer : viewerArray) {
                    String empId = projectDAO.getEmpIdByEname(viewer.trim());
                    if (empId != null && !empId.equals(managerId)) {  // 매니저가 아닌 경우만 추가
                        projectDAO.addProjectMember(projectId, empId, "VIEWER");
                        // 작업 추가 (열람자)
                        projectDAO.createTask(projectId, empId);
                    }
                }
            }

            // 성공 메시지 설정
            req.setAttribute("message", "프로젝트가 생성되었습니다.");
        } else {
            // 실패 시 메시지 설정
            req.setAttribute("message", "프로젝트 생성에 실패했습니다.");
        }

        // 포워드 설정
        ActionForward forward = new ActionForward();
        forward.setPath("/WEB-INF/views/project/project_main.jsp");
        forward.setRedirect(false);
        return forward;
    }

}
