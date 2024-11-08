package com.hta2405.unite.action;

import java.io.IOException;
import java.util.List;
import com.hta2405.unite.dao.DeptDao;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Dept;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.dto.EmpDetails;
import com.hta2405.unite.dto.Job;
import com.hta2405.unite.dao.JobDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EmpViewDeptAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		ActionForward forward = new ActionForward();

		String empId = req.getParameter("empId"); // JSP에서 전달받은 empId

		EmpDao empDao = new EmpDao();
		DeptDao deptDao = new DeptDao();
		JobDao jobDao = new JobDao();

		Emp emp = empDao.getEmpById(empId);
		if (emp == null) {
			throw new ServletException("No employee found with ID: " + empId);
		}

		Long deptId = emp.getDeptId(); // 직원의 부서 ID를 가져옵니다.
		List<Emp> empList = deptDao.getDeptEmps(empId);
		if (empList == null || empList.isEmpty()) {
			throw new ServletException("No employees found in department with ID: " + deptId);
		}

		// 부서와 직무 정보 조회
		Dept dept = deptDao.getDeptByEmpId(empId);
		Job job = jobDao.getJobByEmpId(empId);

		// EmpDetails 객체에 부서, 직무, 직원 정보를 저장합니다.
		EmpDetails details = new EmpDetails();
		details.setEmp(emp);
		details.setDept(dept);
		details.setJob(job);
		details.setEmpList(empList);

		req.setAttribute("details", details);

		forward.setRedirect(false);
		forward.setPath("/WEB-INF/views/empInfo/deptInfo.jsp");

		return forward;
	}
}
