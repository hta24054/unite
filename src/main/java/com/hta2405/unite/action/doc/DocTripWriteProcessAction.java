package com.hta2405.unite.action.doc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.DocDao;
import com.hta2405.unite.dto.DocTrip;
import com.hta2405.unite.enums.DocType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.ToString;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class DocTripWriteProcessAction implements com.hta2405.unite.action.Action {
    DocDao docDao = new DocDao();

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        BufferedReader reader = req.getReader();
        DocTripRequest docTripRequest = gson.fromJson(reader, DocTripWriteProcessAction.DocTripRequest.class);

        LocalDate tripStart = docTripRequest.trip_start != null ? LocalDate.parse(docTripRequest.trip_start) : null;
        LocalDate tripEnd = docTripRequest.trip_end != null ? LocalDate.parse(docTripRequest.trip_end) : null;
        LocalDate cardStart = docTripRequest.card_start != null ? LocalDate.parse(docTripRequest.card_start) : null;
        LocalDate cardEnd = docTripRequest.card_end != null ? LocalDate.parse(docTripRequest.card_end) : null;
        LocalDate cardReturn = docTripRequest.card_return != null ? LocalDate.parse(docTripRequest.card_return) : null;


        System.out.println(docTripRequest);
        DocTrip docTrip = new DocTrip(
                null,
                docTripRequest.writer,
                DocType.TRIP,
                "출장신청서(" + docTripRequest.writer + ")",
                docTripRequest.trip_info,
                LocalDateTime.now(),
                false,
                null,
                tripStart,
                tripEnd,
                docTripRequest.trip_loc,
                docTripRequest.trip_phone,
                docTripRequest.trip_info,
                cardStart,
                cardEnd,
                cardReturn
        );

        int result = docDao.insertTripDoc(docTrip, docTripRequest.signers);
        String status = "success";
        if (result != 1) {
            status = "fail";
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", status);

        resp.getWriter().print(jsonObject);
        return null;
    }
    // 요청 데이터를 매핑할 내부 클래스 정의
    @ToString
    private static class DocTripRequest {
        String writer;
        String trip_start;
        String trip_end;
        String trip_loc;
        String trip_phone;
        String trip_info;
        String card_start;
        String card_end;
        String card_return;
        List<String> signers;
    }
}
