package com.hta2405.unite.action.empInfo;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.*;
import com.hta2405.unite.dto.*;

public class EmpViewAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();

		String empId = (String) session.getAttribute("id");
		String deptId = (String) session.getAttribute("deptId");
		// 세션에서 empId와 deptId 가져옵니다.

		if (req.getParameter("id") != null) {
			empId = req.getParameter("id");
		}

		System.out.println("Received empId from session: " + empId);

		if (empId == null || empId.isEmpty()) {
			throw new ServletException("Employee ID is missing.");
		}

		EmpDao empDao = new EmpDao();
		DeptDao deptDao = new DeptDao();
		JobDao jobDao = new JobDao();
		LangDao langDao = new LangDao();
		CertDao certDao = new CertDao();

		Emp emp = empDao.getEmpById(empId);
		Dept dept = deptDao.getDeptByEmpId(empId);
		Job job = jobDao.getJobByEmpId(empId);
		List<Lang> langList = langDao.getLangByEmpId(empId); // Lang 리스트
		List<Cert> certList = certDao.getCertByEmpId(empId); // Cert 리스트

		if (emp == null) {
			throw new ServletException("No employee found with ID: " + empId);
		}
		if (deptId == null && dept != null) {
			deptId = dept.getDeptId().toString(); // deptId를 문자열로 변환
			session.setAttribute("deptId", deptId);
		}

		EmpDetails details = new EmpDetails();
		details.setEmp(emp);
		details.setDept(dept);
		details.setJob(job);
		details.setLangList(langList); // Lang 리스트 설정
		details.setCertList(certList); // Cert 리스트 설정

		req.setAttribute("details", details);

		ActionForward forward = new ActionForward();
		forward.setRedirect(false);
		forward.setPath("/WEB-INF/views/empInfo/empInfo.jsp");

		return forward;
	}
}
