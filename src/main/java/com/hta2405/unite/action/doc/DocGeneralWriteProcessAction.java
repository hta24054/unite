package com.hta2405.unite.action.doc;

import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.DocDao;
import com.hta2405.unite.dto.Doc;
import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.util.CommonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

public class DocGeneralWriteProcessAction implements com.hta2405.unite.action.Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LocalDateTime dateTime = LocalDateTime.now();
        String writer = req.getParameter("writer");
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        String[] signArr = req.getParameterValues("sign[]");

        Doc doc = new Doc(null, writer, DocType.GENERAL.getType(), title, content, dateTime, false);
        DocDao docDao = new DocDao();
        int result = docDao.insertGeneralDoc(doc, signArr);
        if (result != 1) {
            return CommonUtil.alertAndGoBack(resp, "문서 작성 실패");
        }
        return CommonUtil.alertAndGoBack(resp, "문서 작성 성공");
    }
}
