package com.hta2405.unite.action.project;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ProjectDAO;
import com.hta2405.unite.dto.Emp;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//조직도 부서 및 사원 정보가져오기
public class ProjectEmployAction implements Action {
	private ProjectDAO projectDAO = new ProjectDAO();

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String deptName = request.getParameter("department");
        Long deptId = projectDAO.getDepartmentIdByName(deptName);

        if (deptId == null) {
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "부서를 찾을 수 없습니다.");
            response.getWriter().write(new Gson().toJson(errorResponse));
            return null;
        }

        List<Emp> empList = projectDAO.getEmployeesByDepartment(deptId.intValue());

        // Gson 객체 생성 시 LocalDateAdapter 등록
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new ProjectLocalDateAdapter())
                .create();

        String json = gson.toJson(empList);
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(json);

        return null;
    }
}
