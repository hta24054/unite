package com.hta2405.unite.action.doc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.DocDao;
import com.hta2405.unite.dto.DocBuy;
import com.hta2405.unite.dto.DocBuyItem;
import com.hta2405.unite.enums.DocType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DocBuyWriteProcessAction implements Action {
    DocDao docDao = new DocDao();

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        BufferedReader reader = req.getReader();
        DocBuyRequest docBuyRequest = gson.fromJson(reader, DocBuyRequest.class);

        List<DocBuyItem> items = new ArrayList<>();
        for (DocBuyRequest.Product product : docBuyRequest.productDetails) {
            items.add(new DocBuyItem(
                    null,
                    null,
                    product.product_name,
                    product.standard,
                    product.quantity,
                    product.price
            ));
        }

        DocBuy docBuy = new DocBuy(
                null,
                docBuyRequest.writer,
                DocType.BUY,
                docBuyRequest.title,
                docBuyRequest.content,
                LocalDateTime.now(),
                false,
                null,
                items
        );

        int result = docDao.insertBuyDoc(docBuy, docBuyRequest.signers);

        String status = result == 1 ? "success" : "fail";
        resp.setContentType("application/json");
        resp.getWriter().print("{\"status\":\"" + status + "\"}");
        return null;
    }

    // 요청 데이터를 매핑할 내부 클래스 정의
    private static class DocBuyRequest {
        String writer;
        String title;
        String content;
        List<String> signers;
        List<Product> productDetails;

        private static class Product {
            String product_name;
            String standard;
            long quantity;
            long price;
        }
    }
}
