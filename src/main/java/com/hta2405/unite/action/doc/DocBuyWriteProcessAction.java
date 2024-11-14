package com.hta2405.unite.action.doc;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.DocDao;
import com.hta2405.unite.dto.DocBuy;
import com.hta2405.unite.dto.DocBuyItem;
import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.util.CommonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DocBuyWriteProcessAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] productNames = req.getParameterValues("product_name");
        String[] standards = req.getParameterValues("standard");
        long[] quantities = Arrays.stream(req.getParameterValues("quantity"))
                .map(i -> i.replaceAll(",", ""))
                .mapToLong(Long::parseLong)
                .toArray();
        long[] prices = Arrays.stream(req.getParameterValues("price"))
                .map(i -> i.replaceAll(",", ""))
                .mapToLong(Long::parseLong)
                .toArray();

        List<DocBuyItem> items = new ArrayList<>();
        for (int i = 0; i < productNames.length; i++) {
            items.add(new DocBuyItem(null, null, productNames[i], standards[i], quantities[i], prices[i]));
        }

        DocBuy docBuy = new DocBuy(null,
                req.getParameter("writer"),
                DocType.BUY,
                req.getParameter("title"),
                req.getParameter("content"),
                LocalDateTime.now(),
                false,
                null,
                items
        );

        DocDao docDao = new DocDao();
        String[] signArr = req.getParameterValues("sign[]");
        int result = docDao.insertBuyDoc(docBuy, signArr);
        if (result != 1) {
            return CommonUtil.alertAndGoBack(resp, "문서 작성 실패");
        }
        return new ActionForward(true, req.getContextPath() + "/doc/in-progress");
    }
}
