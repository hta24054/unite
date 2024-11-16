package com.hta2405.unite.action.doc;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.DocDao;
import com.hta2405.unite.dto.DocTrip;
import com.hta2405.unite.dto.DocTripRequest;
import com.hta2405.unite.enums.DocType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DocTripWriteAndEditProcessAction implements Action {
    private final DocDao docDao = new DocDao();

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        BufferedReader reader = req.getReader();
        DocTripRequest docTripRequest = gson.fromJson(reader, DocTripRequest.class);

        LocalDate tripStart = LocalDate.parse(docTripRequest.getTripStart());
        LocalDate tripEnd = LocalDate.parse(docTripRequest.getTripEnd());
        LocalDate cardStart = LocalDate.parse(docTripRequest.getCardStart());
        LocalDate cardEnd = LocalDate.parse(docTripRequest.getCardEnd());
        LocalDate cardReturn = LocalDate.parse(docTripRequest.getCardReturn());

        DocTrip docTrip = new DocTrip(
                docTripRequest.getDocId(),
                docTripRequest.getWriter(),
                DocType.TRIP,
                "출장신청서(" + docTripRequest.getWriter() + ")",
                docTripRequest.getTripInfo(),
                LocalDateTime.now(),
                false,
                docTripRequest.getDocTripId(),
                tripStart,
                tripEnd,
                docTripRequest.getTripLoc(),
                docTripRequest.getTripPhone(),
                docTripRequest.getTripInfo(),
                cardStart,
                cardEnd,
                cardReturn
        );
        System.out.println(docTrip);

        List<String> signList = docTripRequest.getSigners();

        //처음 생성한 문서는 docId null이기 때문에
        if (docTrip.getDocId() == null) {
            insertTripDoc(docTrip, signList, resp);
        } else {
            updateTripDoc(docTrip, signList, resp);
        }
        return null;
    }

    private void insertTripDoc(DocTrip docTrip, List<String> signList, HttpServletResponse resp) throws IOException {
        int result = docDao.insertTripDoc(docTrip, signList);

        String status = result == 1 ? "success" : "fail";
        resp.setContentType("application/json");
        resp.getWriter().print("{\"status\":\"" + status + "\"}");
    }

    private void updateTripDoc(DocTrip docTrip, List<String> signList, HttpServletResponse resp) throws IOException {
        int result = docDao.updateTripDoc(docTrip, signList);

        String status = result == 1 ? "success" : "fail";
        resp.setContentType("application/json");
        resp.getWriter().print("{\"status\":\"" + status + "\"}");
    }
}
