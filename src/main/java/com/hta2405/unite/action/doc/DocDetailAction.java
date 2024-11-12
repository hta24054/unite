package com.hta2405.unite.action.doc;

import com.hta2405.unite.action.ActionForward;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DocDetailAction implements com.hta2405.unite.action.Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String docId = req.getParameter("docId");
        //TODO 내가 기안했거나, 내가 결재자인 경우에만 확인할 수 있는 로직 추가
        return null;
    }
}
