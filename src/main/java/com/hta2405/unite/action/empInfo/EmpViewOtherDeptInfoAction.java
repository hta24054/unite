package com.hta2405.unite.action.empInfo;

import static com.hta2405.unite.util.AttendUtil.checkAttendRole;
import static com.hta2405.unite.util.EmpUtil.isHrDept;

import java.io.IOException;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.util.CommonUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * 타 부서 인사정보 페이지로 포워드하는 액션 클래스입니다.
 */
public class EmpViewOtherDeptInfoAction implements Action {
	private final EmpDao empDao = new EmpDao();

	@Override
	public ActionForward execute(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession();
		String empId = (String) session.getAttribute("id");
		if (empId == null) {
			return CommonUtil.alertAndGoBack(resp,
					"유효하지 않은 세션입니다. 다시 로그인해주세요.");
		}

		Emp emp = empDao.getEmpById(empId);
		if (emp == null) {
			return CommonUtil.alertAndGoBack(resp, "유효하지 않은 직원 ID입니다.");
		}

		if (!isHrDept(emp)) {
			return CommonUtil.alertAndGoBack(resp, "인사부서가 아닙니다.");
		}

		checkAttendRole(emp, req);

		// 타 부서 인사정보 페이지로 포워드 설정을 생성합니다.
		return new ActionForward(false,
				"/WEB-INF/views/empInfo/otherDeptInfo.jsp");
	}
}
