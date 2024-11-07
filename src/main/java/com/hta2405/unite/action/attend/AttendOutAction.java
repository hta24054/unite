package com.hta2405.unite.action.attend;

import com.google.gson.JsonObject;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.AttendDao;
import com.hta2405.unite.util.CommonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

public class AttendOutAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String empId = (String) req.getSession().getAttribute("id");
        AttendDao attendDao = new AttendDao();

        //만약 출근한 기록이 없다면
        if (attendDao.getAttendByEmpId(empId, LocalDate.now()) == null) {
            return CommonUtil.alertAndGoBack(resp, "출근 기록이 없습니다.");
        }

        //출근기록 저장
        int result = attendDao.attendOut(empId);

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
