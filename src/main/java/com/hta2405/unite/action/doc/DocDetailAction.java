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
        return null;
    }
}
