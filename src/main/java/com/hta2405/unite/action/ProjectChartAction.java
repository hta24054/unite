package com.hta2405.unite.action;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

//조직도 팝업창 임시
public class ProjectChartAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	ActionForward forward = new ActionForward();
		forward.setPath("/WEB-INF/views/project/orgChart.jsp"); 
		forward.setRedirect(false);
		return forward;
    }
}
