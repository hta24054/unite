package com.hta2405.unite.action.contact;

import com.google.gson.Gson;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ContactDao;
import com.hta2405.unite.dto.EmpDetails;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ContactViewDeptAction implements Action {
    private ContactDao contactDao = new ContactDao();

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String deptName = req.getParameter("departmentName");

        if (deptName == null || deptName.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("deptName 파라미터가 누락되었습니다.");
            return null;
        }

//        List<EmpDetails> contactList = contactDao.getContactsByDeptName(deptName);

        Gson gson = new Gson();
//        String json = gson.toJson(contactList);
        resp.setContentType("application/json; charset=utf-8");
//        resp.getWriter().write(json);

        return null;
    }
}
