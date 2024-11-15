package com.hta2405.unite.action.doc;

import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.DocDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DocRevokeAction implements com.hta2405.unite.action.Action {
    private final DocDao docDao = new DocDao();

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String empId = (String) req.getSession().getAttribute("id");
        Long docId = Long.parseLong(req.getParameter("docId"));

        String status = "success";

        //결재한적이 없는 사람 또는 문서회수에 실패한다면
        if (!docDao.isDocSignedByEmp(docId, empId) || docDao.revokeDoc(docId, empId) < 1) {
            status = "fail";
        }
        resp.setContentType("application/json");
        resp.getWriter().print("{\"status\":\"" + status + "\"}");
        return null;
    }
}
