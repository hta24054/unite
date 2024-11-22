package com.hta2405.unite.action.schedule;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ScheduleDAO;
import com.hta2405.unite.dto.Holiday;
import com.hta2405.unite.util.LocalDateAdapter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ScheduleGetHolidayAction implements Action {
	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Gson gson = new GsonBuilder()
	                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
	                .create();
		
		String[] startString = req.getParameter("start").split("-");
        String[] endString = req.getParameter("end").split("-");
        LocalDate startDate = LocalDate.of(Integer.parseInt(startString[0]), Integer.parseInt(startString[1]), 1);
        YearMonth endYearMonth = YearMonth.of(Integer.parseInt(endString[0]), Integer.parseInt(endString[1]));
        LocalDate endDate = endYearMonth.atEndOfMonth();
		
        ScheduleDAO scheduleDAO = new ScheduleDAO();
        List<Holiday> holidayList = scheduleDAO.getHoliday(startDate, endDate);
        JsonObject jsonObject = new JsonObject();
        JsonElement je = gson.toJsonTree(holidayList);

        jsonObject.add("holidayList", je);

        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().print(jsonObject);
        return null;
	}
}
