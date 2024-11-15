package com.hta2405.unite.action.doc;

import com.google.gson.Gson;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.DocDao;
import com.hta2405.unite.dto.Doc;
import com.hta2405.unite.dto.DocGeneralRequest;
import com.hta2405.unite.enums.DocType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class DocGeneralWriteAndEditProcessAction implements com.hta2405.unite.action.Action {
    private final DocDao docDao = new DocDao();

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        BufferedReader reader = req.getReader();
        DocGeneralRequest docGeneralRequest = gson.fromJson(reader, DocGeneralRequest.class);

        Doc doc = new Doc(
                docGeneralRequest.getDocId(),
                docGeneralRequest.getWriter(),
                DocType.GENERAL,
                docGeneralRequest.getTitle(),
                docGeneralRequest.getContent(),
                LocalDateTime.now(),
                false
        );
        List<String> signList = docGeneralRequest.getSigners();

        //처음 생성한 문서는 docId null이기 때문에
        if (doc.getDocId() == null) {
            insertGeneralDoc(doc, signList, resp);
        } else {
            updateGeneralDoc(doc, signList, resp);
        }
        return null;
    }

    private void insertGeneralDoc(Doc doc, List<String> signList, HttpServletResponse resp) throws IOException {
        int result = docDao.insertGeneralDoc(doc, signList);

        String status = result == 1 ? "success" : "fail";
        resp.setContentType("application/json");
        resp.getWriter().print("{\"status\":\"" + status + "\"}");
    }

    private void updateGeneralDoc(Doc doc, List<String> signList, HttpServletResponse resp) throws IOException {
        int result = docDao.updateGeneralDoc(doc, signList);

        String status = result == 1 ? "success" : "fail";
        resp.setContentType("application/json");
        resp.getWriter().print("{\"status\":\"" + status + "\"}");
    }
}