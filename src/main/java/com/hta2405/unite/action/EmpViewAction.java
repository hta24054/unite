package com.hta2405.unite.action;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.hta2405.unite.dao.*;
import com.hta2405.unite.dto.*;

public class EmpViewAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		/*
		 * String empId = req.getParameter("id");
		if (empId == null || empId.isEmpty()) {
			throw new ServletException("Employee ID is missing.");
		}
		*/
		 // 임시로 테스트하기 위해 하드코딩된 empId 사용
	    String empId = "E002"; // 추후 삭제 또는 변경

		EmpInfoDao dao = new EmpInfoDao();
		EmpInfo empinfo = dao.getEmpInfoById(empId);

		if (empinfo == null) {
			throw new ServletException("No employee found with ID: " + empId);
		}

		req.setAttribute("empinfo", empinfo);

		ActionForward forward = new ActionForward();
		forward.setRedirect(false);
		forward.setPath("/WEB-INF/views/empInfo/empInfo.jsp");

		return forward;
	}
}