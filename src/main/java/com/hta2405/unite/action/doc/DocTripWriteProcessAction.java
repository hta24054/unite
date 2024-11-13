package com.hta2405.unite.action.doc;

import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.DocDao;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.DocTrip;
import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.util.CommonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DocTripWriteProcessAction implements com.hta2405.unite.action.Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String writer = req.getParameter("writer");
        String title = "출장신청서(" + new EmpDao().getEmpById(writer).getEname() + ")";

        DocTrip docTrip = new DocTrip(null,
                req.getParameter("writer"),
                DocType.TRIP,
                title,
                null,
                LocalDateTime.now(),
                false,
                null,
                LocalDate.parse(req.getParameter("trip_start")),
                LocalDate.parse(req.getParameter("trip_end")),
                req.getParameter("trip_loc"),
                req.getParameter("trip_phone"),
                req.getParameter("trip_info"),
                LocalDate.parse(req.getParameter("card_start")),
                LocalDate.parse(req.getParameter("card_end")),
                LocalDate.parse(req.getParameter("card_return"))
        );

        DocDao docDao = new DocDao();
        String[] signArr = req.getParameterValues("sign[]");

        int result = docDao.insertTripDoc(docTrip, signArr);

        if (result != 1) {
            return CommonUtil.alertAndGoBack(resp, "문서 작성 실패");
        }
        return CommonUtil.alertAndGoBack(resp, "문서 작성 성공");
    }
}
