package com.hta2405.unite.action;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
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
        String deptName = request.getParameter("department"); // 부서 이름을 가져옴
        Long deptId = projectDAO.getDepartmentIdByName(deptName); // 부서 이름으로 ID 가져오기

        if (deptId == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "부서를 찾을 수 없습니다."); // 부서가 없을 경우 처리
            return null;
        }

        List<Emp> empList = projectDAO.getEmployeesByDepartment(deptId.intValue());

        String json = new Gson().toJson(empList);
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(json);
        System.out.println("Department ID: " + deptId);
        System.out.println("Employees List: " + empList);
        System.out.println("json: " + json);

        return null; // 리다이렉트가 필요 없는 경우
    }
}
