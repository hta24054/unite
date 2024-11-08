package com.hta2405.unite.action.admin;

import com.google.gson.JsonObject;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.NoticeDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AdminNoticeDeleteAction implements com.hta2405.unite.action.Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int result = new NoticeDao().deleteById(Long.parseLong(req.getParameter("noticeId")));
        String message = "공지사항 삭제를 성공하였습니다.";

        if (result != 1) {
            message = "공지사항 삭제 실패";
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", message);
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().print(jsonObject);
        return null;
    }
}
