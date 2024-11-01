package com.hta2405.unite.action;

import java.io.IOException;
import java.sql.SQLException;

import com.hta2405.unite.dao.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.hta2405.unite.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EmpInfoUpdateAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ActionForward forward = new ActionForward();

		EmpInfoDao dao = new EmpInfoDao();
		EmpInfo empinfo = new EmpInfo();
		empinfo.setEmpId(req.getParameter("id"));
		empinfo.setEname(req.getParameter("name"));

		try {
			dao.updateEmpInfo(empinfo);
		} catch (SQLException e) {

			e.printStackTrace();
		}
		forward.setPath("/empInfo/view?id=" + empinfo.getEmpId());
		forward.setRedirect(false);
		return forward;
	}

}
