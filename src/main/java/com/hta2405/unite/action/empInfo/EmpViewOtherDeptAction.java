package com.hta2405.unite.action.empInfo;

import java.io.IOException;
import java.util.HashMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.DeptDao;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.util.LocalDateAdapter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

public class EmpViewOtherDeptAction implements Action {
	private DeptDao deptDao = new DeptDao();

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// AJAX 요청에서 전달된 부서명 파라미터 확인
		String deptName = req.getParameter("departmentName"); // departmentName으로 수정
		System.out.println("서버 요청: 부서명 - " + deptName);
		Long deptId = deptDao.getDeptIdByDeptName(deptName);

		// DeptDao의 getIdToDeptNameMap 메서드를 사용하여 부서 ID 조회

		System.out.println("부서 ID - " + deptId);

		// 부서 ID가 null인 경우 에러 응답 반환
		if (deptId == null) {
			resp.setContentType("application/json; charset=utf-8");
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", "부서를 찾을 수 없습니다.");
			resp.getWriter().write(new Gson().toJson(errorResponse));
			return null;
		}

		// 부서 ID를 사용하여 해당 부서의 직원 목록 조회
		List<Emp> empList = deptDao.getEmployeesByDepartment(deptId.intValue());
		System.out.println("조회된 직원 리스트 - " + empList);

		// 직원 목록을 JSON 형식으로 응답
		// 직원 목록을 JSON 형식으로 응답
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(LocalDate.class, new LocalDateAdapter()) 
				.create();
		String json = gson.toJson(empList);
		resp.setContentType("application/json; charset=utf-8"); 
		resp.getWriter().write(json);

		return null;
	}
}