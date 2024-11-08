package com.hta2405.unite.action.admin;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.NoticeDao;
import com.hta2405.unite.dto.Notice;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class AdminNoticeAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Notice> noticeList = new NoticeDao().getAllNotice();
        req.setAttribute("noticeList", noticeList);
        return new ActionForward(false, "/WEB-INF/views/admin/notice.jsp");
    }
}
