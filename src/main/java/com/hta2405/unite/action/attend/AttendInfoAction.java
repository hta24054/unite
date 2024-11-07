package com.hta2405.unite.action.attend;

import com.google.gson.JsonObject;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.AttendDao;
import com.hta2405.unite.dto.Attend;
import com.hta2405.unite.util.CommonUtil;
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

        if (empId == null) {
            return CommonUtil.alertAndGoBack(resp, "유저가 없습니다.");
        }

        AttendDao attendDao = new AttendDao();
        Attend attend = attendDao.getAttendByEmpId(empId, LocalDate.now());

        resp.setContentType("application/json");
        JsonObject jsonObject = createAttendJsonResponse(attend);

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