package com.hta2405.unite.action;

import java.io.IOException;
import java.io.PrintWriter;

import com.hta2405.unite.dao.ProjectDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//프로젝트 상세. main에서 누르고 들어오는 첫 창
public class ProjectUpdateRateAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 클라이언트로부터 전달받은 파라미터 처리
        int projectId = Integer.parseInt(req.getParameter("projectId"));
        String memberId = req.getParameter("memberId");
        int progressRateParam = Integer.parseInt(req.getParameter("memberProgressRate"));
        
        // 데이터베이스에서 진행률 업데이트
        ProjectDAO projectDAO = new ProjectDAO();
        boolean success = projectDAO.updateProgressRate(projectId, memberId, progressRateParam);

        // 응답할 JSON 형식의 문자열 생성
        StringBuilder jsonResponse = new StringBuilder();
        jsonResponse.append("{");
        jsonResponse.append("\"success\":").append(success);
        jsonResponse.append("}");
        
        // 응답 헤더 설정 및 JSON 반환
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        // 응답으로 JSON 문자열을 실제로 보내기
        PrintWriter out = resp.getWriter();
        out.print(jsonResponse.toString());
        out.flush();  // 출력 스트림을 플러시하여 응답이 전송되도록 함
        
        return null; // AJAX 응답만 처리
    }

}
