package com.hta2405.unite.action.doc;

import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.DocDao;
import com.hta2405.unite.dto.Doc;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DocDeleteAction implements com.hta2405.unite.action.Action {
    private final DocDao docDao = new DocDao();

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String empId = (String) req.getSession().getAttribute("id");
        Long docId = Long.parseLong(req.getParameter("docId"));
        Doc doc = docDao.getGeneralDocByDocId(docId);


        String status = "success";
        /*
            작성자가 아니거나, 현재 로그인 한 사람이 결재자순번이 아니거나, 문서가 결재가 완료되었으면 fail
            즉, 로그인한 사람이 작성자면서 + 그 사람이 결재순번인 경우 + 문서결재가 완료되지 않은 경우만 success
         */
        if (!doc.getDocWriter().equals(empId)
                || !docDao.getNowSigner(docId).equals(empId)
                || doc.isSignFinish()) {
            status = "fail";
        }
        if(docDao.deleteDoc(docId))
        resp.setContentType("application/json");
        resp.getWriter().print("{\"status\":\"" + status + "\"}");
        return null;
    }
}
