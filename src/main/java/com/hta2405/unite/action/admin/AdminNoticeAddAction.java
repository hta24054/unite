package com.hta2405.unite.action.admin;

import com.google.gson.JsonObject;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.NoticeDao;
import com.hta2405.unite.dto.Notice;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

public class AdminNoticeAddAction implements com.hta2405.unite.action.Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LocalDate endDate = LocalDate.parse(req.getParameter("endDate"));
        String subject = req.getParameter("subject");
        String content = req.getParameter("content");
        Notice notice = new Notice(null, subject, content, endDate);
        int result = new NoticeDao().insertNotice(notice);
        String message = "공지사항 등록을 성공하였습니다.";

        if (result != 1) {
            message = "공지사항 등록 실패";
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", message);
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().print(jsonObject);
        return null;
    }
}