package com.hta2405.unite.action.doc;

import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.DeptDao;
import com.hta2405.unite.dao.DocDao;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.*;
import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.util.CommonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class DocEditAction implements com.hta2405.unite.action.Action {
    private final DocDao docDao = new DocDao();
    private final EmpDao empDao = new EmpDao();
    private final DeptDao deptDao = new DeptDao();

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loginEmpId = (String) req.getSession().getAttribute("id");
        Long docId = Long.parseLong(req.getParameter("docId"));
        Doc doc = docDao.getGeneralDocByDocId(docId);

        if (!isValidToEdit(loginEmpId, doc)) {
            return CommonUtil.alertAndGoBack(resp, "수정 권한이 없습니다.");
        }
        //공통 attr 추가
        setCommonAttr(req, doc, docId);

        return getEditActionForward(req, docId);
    }

    private void setCommonAttr(HttpServletRequest req, Doc doc, Long docId) {
        //작성자 및 부서 이름 정보 추가
        req.setAttribute("writer", empDao.getEmpById(doc.getDocWriter()));
        req.setAttribute("dept", deptDao.getDeptByEmpId(doc.getDocWriter()));

        //결재자 정보 추가
        List<Sign> signList = docDao.getSignListByDocId(docId);
        req.setAttribute("signList", signList);
        System.out.println(signList);

        //결재자 이름 표시를 위한 Map 추가
        HashMap<String, String> nameMap = new HashMap<>();
        for (Sign sign : signList) {
            nameMap.put(sign.getEmpId(), empDao.getEmpById(sign.getEmpId()).getEname());
        }
        req.setAttribute("nameMap", nameMap);
    }

    private boolean isValidToEdit(String empId, Doc doc) {
        return empId.equals(doc.getDocWriter())
                && !doc.isSignFinish()
                && docDao.getNowSigner(doc.getDocId()).equals(empId);
    }
    //문서 종류 가져오기
    private Doc getDetailedDocById(Long docId) {
        Doc doc = docDao.getGeneralDocByDocId(docId);
        DocType docType = doc.getDocType();

        return switch (docType) {
            case GENERAL -> doc;
            case BUY -> docDao.getBuyDoc(docId);
            case TRIP -> docDao.getTripDocById(docId);
            case VACATION -> docDao.getVacationDoc(docId);
        };
    }

    private ActionForward getEditActionForward(HttpServletRequest req, Long docId) {
        Doc doc = getDetailedDocById(docId);
        if (doc instanceof DocBuy docBuy) {
            req.setAttribute("doc", docBuy);
            return new ActionForward(false, "/WEB-INF/views/doc/buy_edit.jsp");
        }

        if (doc instanceof DocTrip docTrip) {
            req.setAttribute("doc", docTrip);
            return new ActionForward(false, "/WEB-INF/views/doc/trip_edit.jsp");
        }

        if (doc instanceof DocVacation docVacation) {
            req.setAttribute("doc", docVacation);
            return new ActionForward(false, "/WEB-INF/views/doc/vacation_edit.jsp");
        }
        //일반 문서 페이지로 포워딩
        req.setAttribute("doc", doc);
        return new ActionForward(false, "/WEB-INF/views/doc/general_edit.jsp");
    }


}