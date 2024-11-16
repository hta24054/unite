package com.hta2405.unite.action.doc;

import com.google.gson.Gson;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.DocDao;
import com.hta2405.unite.dto.DocBuy;
import com.hta2405.unite.dto.DocBuyItem;
import com.hta2405.unite.dto.DocBuyRequest;
import com.hta2405.unite.enums.DocType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DocBuyWriteAndEditProcessAction implements Action {
    DocDao docDao = new DocDao();

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        BufferedReader reader = req.getReader();
        DocBuyRequest docBuyRequest = gson.fromJson(reader, DocBuyRequest.class);

        List<DocBuyItem> items = new ArrayList<>();
        for (DocBuyRequest.Product product : docBuyRequest.getProductDetails()) {
            items.add(new DocBuyItem(
                    null,
                    docBuyRequest.getDocBuyId(),
                    product.getProduct_name(),
                    product.getStandard(),
                    product.getQuantity(),
                    product.getPrice()
            ));
        }
        System.out.println(items);

        DocBuy doc = new DocBuy(
                docBuyRequest.getDocId(),
                docBuyRequest.getWriter(),
                DocType.BUY,
                docBuyRequest.getTitle(),
                docBuyRequest.getContent(),
                LocalDateTime.now(),
                false,
                docBuyRequest.getDocBuyId(),
                items
        );

        List<String> signList = docBuyRequest.getSigners();

        //처음 생성한 문서는 docId null이기 때문에
        if (doc.getDocId() == null) {
            insertBuyDoc(doc, signList, resp);
        } else {
            updateBuyDoc(doc, signList, resp);
        }
        return null;
    }

    private void insertBuyDoc(DocBuy doc, List<String> signList, HttpServletResponse resp) throws IOException {
        int result = docDao.insertBuyDoc(doc, signList);

        String status = result == 1 ? "success" : "fail";
        resp.setContentType("application/json");
        resp.getWriter().print("{\"status\":\"" + status + "\"}");
    }

    private void updateBuyDoc(DocBuy doc, List<String> signList, HttpServletResponse resp) throws IOException {
        int result = docDao.updateBuyDoc(doc, signList);

        String status = result == 1 ? "success" : "fail";
        resp.setContentType("application/json");
        resp.getWriter().print("{\"status\":\"" + status + "\"}");
    }
}
