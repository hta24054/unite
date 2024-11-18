package com.hta2405.unite.action.doc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.DocDao;
import com.hta2405.unite.dto.DocWithSigner;
import com.hta2405.unite.enums.AttendType;
import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.util.DocTypeAdapter;
import com.hta2405.unite.util.LocalDateTimeAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class DocWaitingListProcessAction implements com.hta2405.unite.action.Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String empId = (String) req.getSession().getAttribute("id");
        List<DocWithSigner> list = new DocDao().getWaitingListByEmpId(empId);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DocType.class, new DocTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        JsonElement jsonTree = gson.toJsonTree(list);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("list", jsonTree);
        resp.setContentType("application/json; charset=UTF-8");

        resp.getWriter().print(jsonObject);

        return null;
    }
}
