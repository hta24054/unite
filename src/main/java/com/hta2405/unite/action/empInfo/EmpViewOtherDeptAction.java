package com.hta2405.unite.action.empInfo;

import java.io.IOException;
import java.util.HashMap;
import com.google.gson.Gson;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.DeptDao;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Emp;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

public class EmpViewOtherDeptAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // AJAX 요청에서 전달된 부서명 파라미터 확인
        String deptName = req.getParameter("departmentName");
        System.out.println("서버 요청: 부서명 - " + deptName);

        // DeptDao의 getIdToDeptNameMap 메서드를 사용하여 부서 ID 조회
        DeptDao deptDao = new DeptDao();
        HashMap<Long, String> deptMap = deptDao.getIdToDeptNameMap();
        Long deptId = null;
        
        // 부서명을 통해 부서 ID를 찾음
        for (HashMap.Entry<Long, String> entry : deptMap.entrySet()) {
            if (entry.getValue().equals(deptName)) {
                deptId = entry.getKey();
                break;
            }
        }
        System.out.println("부서 ID - " + deptId);

        // 부서 ID가 null인 경우 에러 응답 반환
        if (deptId == null) {
            resp.setContentType("application/json; charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\":\"Department not found.\"}");
            return null;
        }

        // 부서 ID를 사용하여 해당 부서의 직원 목록 조회
        EmpDao empDao = new EmpDao();
        List<Emp> empList = empDao.getEmpByDeptId(deptId); // 부서 ID로 직원 목록 조회
        System.out.println("조회된 직원 리스트 - " + empList);

        // 직원 목록을 JSON 형식으로 응답
        String json = new Gson().toJson(empList);
        resp.setContentType("application/json; charset=utf-8");
        resp.getWriter().write(json);

        return null;
    }
}
