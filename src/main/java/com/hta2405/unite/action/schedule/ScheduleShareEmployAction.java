package com.hta2405.unite.action.schedule;

import java.io.IOException;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ScheduleDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ScheduleShareEmployAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		ScheduleDAO sdao = new ScheduleDAO();
		
		// 이름, 직급, 부서, 사원ID
		String deptName = request.getParameter("department");
	
		return null;
	
		
		
	}

}
