package com.hta2405.unite.action;

import com.google.gson.JsonObject;
import com.hta2405.unite.dao.AttendDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

public class AttendInAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String empId = (String) req.getSession().getAttribute("id");

        AttendDao attendDao = new AttendDao();
        //만약 출근한적이 있다면
        if (attendDao.getAttendByEmpId(empId, LocalDate.now()) != null) {
            System.out.println("이미 출근하였습니다.");
            return null;
        }
        //출근기록 저장
        int result = attendDao.attendIn(empId, req.getParameter("attendType"));

        String status = result == 1 ? "success" : "fail";

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", status);

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(jsonObject);
        out.flush();
        return null;
    }
}
