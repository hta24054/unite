package com.hta2405.unite.action.emp;

import java.io.IOException;
import java.util.List;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.hta2405.unite.dao.*;
import com.hta2405.unite.dto.*;

public class EmpViewAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String id = (String) req.getSession().getAttribute("id");

		System.out.println("Received id: " + id);

		if (id == null || id.isEmpty()) {
			throw new ServletException("Employee ID is missing.");
		}

		// 추후 삭제 또는 변경

		EmpDao empDao = new EmpDao();
		DeptDao deptDao = new DeptDao();
		JobDao jobDao = new JobDao();
		LangDao langDao = new LangDao();
		CertDao certDao = new CertDao();

		Emp emp = empDao.getEmpById(id);
		Dept dept = deptDao.getDeptByEmpId(id);
		Job job = jobDao.getJobByEmpId(id);
		List<Lang> langList = langDao.getLangByEmpId(id); // Lang 리스트
		List<Cert> certList = certDao.getCertByEmpId(id); // // Cert 리스트

		if (emp == null) {
			throw new ServletException("No employee found with ID: " + id);
		}

		EmpDetails details = new EmpDetails();
		details.setEmp(emp);
		details.setDept(dept);
		details.setJob(job);
		details.setLangList(langList); // Lang 리스트 설정
		details.setCertList(certList); // Cert 리스트 설정
		if (!langList.isEmpty())
			details.setLang(langList.get(0));
		if (!certList.isEmpty())
			details.setCert(certList.get(0));
		req.setAttribute("details", details);

		ActionForward forward = new ActionForward();
		forward.setRedirect(false);
		forward.setPath("/WEB-INF/views/empInfo/empInfo.jsp");
		return forward;
	}
}
