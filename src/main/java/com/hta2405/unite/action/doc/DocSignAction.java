package com.hta2405.unite.action.doc;

import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.AttendDao;
import com.hta2405.unite.dao.DocDao;
import com.hta2405.unite.dto.Doc;
import com.hta2405.unite.dto.DocTrip;
import com.hta2405.unite.dto.DocVacation;
import com.hta2405.unite.enums.DocType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DocSignAction implements com.hta2405.unite.action.Action {
    private final DocDao docDao = new DocDao();
    private final AttendDao attendDao = new AttendDao();

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String empId = (String) req.getSession().getAttribute("id");
        Long docId = Long.parseLong(req.getParameter("docId"));

        String status = "success";
        if (!docDao.getNowSigner(docId).equals(empId) || docDao.signDoc(docId, empId) != 1) {
            status = "fail";
        }

        Doc doc = docDao.getGeneralDocByDocId(docId);
        if (doc.isSignFinish()) {
            if (doc.getDocType() == DocType.VACATION) {
                DocVacation vacationDoc = docDao.getVacationDoc(docId);
                boolean result = attendDao.insertVacation(vacationDoc);
                if (!result) {
                    status = "fail";
                }
            } else if (doc.getDocType() == DocType.TRIP) {
                DocTrip tripDoc = docDao.getTripDocById(docId);
                boolean result = attendDao.insertTrip(tripDoc);
                if (!result) {
                    status = "fail";
                }
            }
        }

        resp.setContentType("application/json");
        resp.getWriter().print("{\"status\":\"" + status + "\"}");
        return null;
    }
}