	package com.hta2405.unite.action.project;
	
	import java.io.IOException;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ProjectDAO;
	
	import jakarta.servlet.ServletException;
	import jakarta.servlet.http.HttpServletRequest;
	import jakarta.servlet.http.HttpServletResponse;
	
	//메인 db값 가져오기. 추후 관리자만 관리 창 뜨게 수정 필요
	public class ProjectMainAction implements Action {
	    @Override
	    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	        String action = req.getParameter("action");
	
	        // 상태 업데이트 요청 처리
	        if ("updateStatus".equals(action)) {
	            int projectId = Integer.parseInt(req.getParameter("projectId"));
	            String status = req.getParameter("status");
	
	            ProjectDAO projectDAO = new ProjectDAO();
	            boolean success = false;
	
	            // 프로젝트 상태에 따라 업데이트
	            if ("completed".equals(status)) {
	                success = projectDAO.updateProjectStatus(projectId, true, false); // 완료
	            } else if ("cancelled".equals(status)) {
	                success = projectDAO.updateProjectStatus(projectId, false, true); // 취소
	            }
	
	            // JSON 응답 반환
	            resp.setContentType("application/json");
	            resp.getWriter().write("{\"success\":" + success + "}");
	            return null; // AJAX 호출이므로 포워딩 필요 없음
	        }
	
	        // 일반 페이지 요청 처리
	        ActionForward forward = new ActionForward();
	        forward.setPath("/WEB-INF/views/project/project_main.jsp"); // 프로젝트 메인 페이지로 이동
	        forward.setRedirect(false);
	        return forward;
	    }
	} 