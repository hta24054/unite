package com.hta2405.unite.action;

import com.google.gson.JsonObject;
import com.hta2405.unite.dao.AttendDao;
import com.hta2405.unite.dto.Attend;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

public class AttendInfoAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String empId = (String) req.getSession().getAttribute("id");

        // empId가 null인 경우
        if (empId == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "유저가 없습니다.");
            return null;
        }

        AttendDao attendDao = new AttendDao();
        Attend attend = attendDao.getAttendByEmpId(empId, LocalDate.now());

        // JSON 응답 준비
        resp.setContentType("application/json");
        JsonObject jsonObject = createAttendJsonResponse(attend);

        // JSON 응답 출력
        try (PrintWriter out = resp.getWriter()) {
            out.print(jsonObject);
            out.flush();
        }

        return null;
    }

    /**
     * 출근 및 퇴근 상태에 따라 JSON 응답 객체 생성
     *
     * @param attend 현재 출근 상태를 나타내는 Attend 객체
     * @return JSON 응답 객체
     */
    private JsonObject createAttendJsonResponse(Attend attend) {
        JsonObject jsonObject = new JsonObject();

        if (attend == null) {
            // 출근 기록이 없는 경우
            jsonObject.addProperty("status", "empty");
        } else if (attend.getAttendOut() == null) {
            // 출근 기록만 있고 퇴근 기록이 없는 경우
            jsonObject.addProperty("status", "checkIn");
            jsonObject.addProperty("inTime", String.valueOf(attend.getAttendIn()));
            jsonObject.addProperty("type", attend.getAttendType());
        } else {
            // 출근과 퇴근 기록이 모두 있는 경우
            jsonObject.addProperty("status", "checkOut");
            jsonObject.addProperty("inTime", String.valueOf(attend.getAttendIn()));
            jsonObject.addProperty("outTime", String.valueOf(attend.getAttendOut()));
            jsonObject.addProperty("type", attend.getAttendType());
        }

        return jsonObject;
    }
}