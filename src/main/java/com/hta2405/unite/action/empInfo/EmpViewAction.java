package com.hta2405.unite.action.empInfo;

import static com.hta2405.unite.util.AttendUtil.checkAttendRole;
import static com.hta2405.unite.util.EmpUtil.isHrDept;

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

/**
 * 직원 정보 조회를 위한 액션 클래스입니다. 요청된 직원 ID에 따라 직원의 상세 정보를 조회합니다.
 */
public class EmpViewAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();

		// 세션에서 empId와 deptId를 가져옵니다.
		String empId = (String) session.getAttribute("id");
		String deptId = (String) session.getAttribute("deptId");
		System.out.println("deptId" + deptId);
		System.out.println("empId" + empId);
		// 요청 파라미터에 id가 있으면 해당 값을 사용합니다.
		if (req.getParameter("id") != null) {
			empId = req.getParameter("id");
		}

		System.out.println("Received empId from session: " + empId);

		// empId가 없으면 에러를 발생시킵니다.
		if (empId == null || empId.isEmpty()) {
			throw new ServletException("Employee ID is missing.");
		}

		// DAO 객체를 생성합니다.
		EmpDao empDao = new EmpDao();
		DeptDao deptDao = new DeptDao();
		JobDao jobDao = new JobDao();
		LangDao langDao = new LangDao();
		CertDao certDao = new CertDao();

		// 직원, 부서, 직책, 언어, 자격증 정보를 조회합니다.
		Emp emp = empDao.getEmpById(empId);
		Dept dept = deptDao.getDeptByEmpId(empId);
		Job job = jobDao.getJobByEmpId(empId);
		List<Lang> langList = langDao.getLangByEmpId(empId);
		List<Cert> certList = certDao.getCertByEmpId(empId);

		// 직원 정보가 없으면 에러를 발생시킵니다.
		if (emp == null) {
			throw new ServletException("No employee found with ID: " + empId);
		}

		// 세션에 부서 ID를 설정합니다.
		if (deptId == null && dept != null) {
			deptId = dept.getDeptId().toString();
			session.setAttribute("deptId", deptId);
			
		}

		// EmpDetails 객체를 생성하고 필요한 정보를 설정합니다.
		EmpDetails details = new EmpDetails();
		details.setEmp(emp);
		details.setDept(dept);
		details.setJob(job);
		details.setLangList(langList);
		details.setCertList(certList);

		// 요청 속성에 EmpDetails 객체를 설정합니다.
		req.setAttribute("details", details);

		// 포워드 설정을 생성하고 경로를 설정합니다.
		ActionForward forward = new ActionForward();
		forward.setRedirect(false);
		forward.setPath("/WEB-INF/views/empInfo/empInfo.jsp");

		checkAttendRole(emp, req);
		long jobRank = jobDao.getJobRankByEmpId(empId);
		boolean is = isHrDept(deptId); //참 이면 부서인사정보 메뉴 보이게
									//부서인사정보는 인사관리팀 이거나 jobRank<=3
									//타 부서 인사정보는 인사관리팀만 보여야 됨
		System.out.println("is" + is);
		System.out.println("jobRank" + jobRank);
		session.setAttribute("deptShow",is || jobRank<=4);
		session.setAttribute("otherDeptShow",is);
		
		return forward;
	}
}
