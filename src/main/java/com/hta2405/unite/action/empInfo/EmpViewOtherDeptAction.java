package com.hta2405.unite.action.empInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;

import com.hta2405.unite.dao.DeptDao;
import com.hta2405.unite.dao.JobDao;
import com.hta2405.unite.dto.Emp;

import com.hta2405.unite.util.LocalDateAdapter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 타 부서 직원 정보를 조회하는 액션 클래스입니다. 요청된 부서명에 따라 해당 부서의 직원 목록을 JSON 형식으로 반환합니다.
 */
public class EmpViewOtherDeptAction implements Action {

	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			long deptId = Long.parseLong(req.getParameter("deptId"));

			DeptDao deptDao = new DeptDao();

			JsonObject jsonObject = new JsonObject();

			Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

			// 부서 ID를 사용하여 해당 부서의 직원 목록 조회
			List<Emp> empList = new DeptDao().getEmployeesByDepartment(deptId);
			JsonElement listToJson = gson.toJsonTree(empList);
			jsonObject.add("empList", listToJson);

			String deptName1 = deptDao.getDeptNameById(deptId);
			jsonObject.addProperty("deptName1", deptName1);

			HashMap<Long, String> jobNameMap = new JobDao().getIdToJobNameMap();
			JsonElement mapToJson = gson.toJsonTree(jobNameMap);
			jsonObject.add("jobName", mapToJson);

			HashMap<Long, String> deptNameMap = new DeptDao().getIdToDeptNameMap();
			JsonElement mapToJsonDeptName = gson.toJsonTree(deptNameMap);
			jsonObject.add("deptName", mapToJsonDeptName);

			resp.setContentType("application/json;charset=utf-8");
			resp.getWriter().print(jsonObject);

		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().write("Error processing request");
		}
		return null;
	}
}
