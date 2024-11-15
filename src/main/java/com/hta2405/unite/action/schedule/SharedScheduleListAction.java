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
		
        String empId = request.getParameter("emp_id"); // 로그인된 사용자 ID

        ScheduleDAO sdao = new ScheduleDAO();
        JsonArray arr = sdao.getListSharedSchedule(empId);

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(arr.toString());
        
        System.out.println("공유 리스트" + arr); 
        return null;
	}
}
