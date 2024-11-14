package com.hta2405.unite.action.schedule;

import java.io.IOException;

import com.google.gson.JsonArray;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ScheduleDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SharedScheduleListAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
        String empId = request.getParameter("emp_id"); // 일정 등록자 ID (로그인된 사용자)
        String[] shareEmpArray = request.getParameterValues("share_emp"); // 공유자 ID (선택된 직원들)
        if (shareEmpArray == null) {
            shareEmpArray = new String[0]; // 기본값으로 빈 배열 설정
        }
  
        ScheduleDAO sdao = new ScheduleDAO();
        JsonArray arr = sdao.getSharedScheduleList(empId, shareEmpArray);

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(arr.toString());
        
        System.out.println("공유 리스트" + arr); 
        return null;
	}
}
