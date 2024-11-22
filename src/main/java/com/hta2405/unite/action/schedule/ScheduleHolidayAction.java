package com.hta2405.unite.action.schedule;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ScheduleDAO;
import com.hta2405.unite.dto.Holiday;
import com.hta2405.unite.util.LocalDateAdapter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ScheduleHolidayAction implements Action {
	
	@Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //올해 1월 1일부터, 현재부터 1년 후 시점까지의 휴일을 가져옴
        int year = LocalDate.now().getYear();
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.now().plusYears(1);
        
        ScheduleDAO scheduleDao = new ScheduleDAO();
        List<Holiday> yearlyHoliday = scheduleDao.getHoliday(startDate, endDate);
        
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        String jsonResponse = gson.toJson(yearlyHoliday);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        resp.getWriter().write(jsonResponse);
        
        return null;
    }
}
