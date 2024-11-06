package com.hta2405.unite.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.hta2405.unite.dao.ScheduleDAO;
import com.hta2405.unite.dto.Schedule;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ScheduleListAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		ScheduleDAO sdao = new ScheduleDAO();
		List<Schedule> scheduleList = new ArrayList<Schedule>();
		
		sdao.getListSchedule();
		
		// 일정 목록을 JSON으로 변환
        Gson gson = new Gson();
        String jsonSchedules = gson.toJson(scheduleList);
        
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(jsonSchedules);
        return null;
	}

}
