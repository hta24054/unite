package com.hta2405.unite.action.doc;

import com.google.gson.Gson;
import com.hta2405.unite.action.Action;
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


public class DocTripWriteProcessAction implements Action {
    DocDao docDao = new DocDao();

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        BufferedReader reader = req.getReader();
        DocTripRequest docTripRequest = gson.fromJson(reader, DocTripWriteProcessAction.DocTripRequest.class);

        LocalDate tripStart = LocalDate.parse(docTripRequest.trip_start);
        LocalDate tripEnd = LocalDate.parse(docTripRequest.trip_end);
        LocalDate cardStart = LocalDate.parse(docTripRequest.card_start);
        LocalDate cardEnd = LocalDate.parse(docTripRequest.card_end);
        LocalDate cardReturn = LocalDate.parse(docTripRequest.card_return);


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
        String status = result == 1 ? "success" : "fail";
        resp.setContentType("application/json");
        resp.getWriter().print("{\"status\":\"" + status + "\"}");
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
