package com.hta2405.unite.action.doc;

import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.DocDao;
import com.hta2405.unite.dto.DocWithSigner;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class DocWaitingListAction implements com.hta2405.unite.action.Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String empId = (String) req.getSession().getAttribute("id");

        List<DocWithSigner> list = new DocDao().getWaitingListByEmpId(empId);
        req.setAttribute("list", list);
        return new ActionForward(false, "/WEB-INF/views/doc/waiting.jsp");
    }
}
