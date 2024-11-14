package com.hta2405.unite.action.doc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.DocDao;
import com.hta2405.unite.dto.Doc;
import com.hta2405.unite.enums.DocType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class DocGeneralWriteProcessAction implements com.hta2405.unite.action.Action {
    private final DocDao docDao = new DocDao();

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        BufferedReader reader = req.getReader();
        DocGeneralWriteProcessAction.DocGeneralRequest docBuyRequest = gson.fromJson(reader, DocGeneralWriteProcessAction.DocGeneralRequest.class);

        Doc doc = new Doc(
                null,
                docBuyRequest.writer,
                DocType.GENERAL,
                docBuyRequest.title,
                docBuyRequest.content,
                LocalDateTime.now(),
                false
        );

        int result = docDao.insertGeneralDoc(doc, docBuyRequest.signers);
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
    private static class DocGeneralRequest {
        String writer;
        String title;
        String content;
        List<String> signers;
    }
}