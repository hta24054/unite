package com.hta2405.unite.action.doc;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.DeptDao;
import com.hta2405.unite.dao.DocDao;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.*;
import com.hta2405.unite.enums.DocRole;
import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.util.CommonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DocReadAction implements Action {
    private final DocDao docDao = new DocDao();
    private final EmpDao empDao = new EmpDao();
    private final DeptDao deptDao = new DeptDao();

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loginEmpId = (String) req.getSession().getAttribute("id");
        Long docId = Long.parseLong(req.getParameter("docId"));

        //문서 열람 권한 확인
        if (!isValidToAccessDoc(loginEmpId, docId)) {
            return CommonUtil.alertAndGoBack(resp, "문서 조회 권한이 없습니다.");
        }
        Doc doc = docDao.getGeneralDocByDocId(docId);
        setCommonAttr(req, doc, docId);

        if (doc.isSignFinish()) {
            req.setAttribute("role", DocRole.VIEWER.getType());
        } else {
            //결재 미 완료 시 권한 설정 -> 결재할 사람인지, 단순 조회인지
            DocRole docRole = checkRole(loginEmpId, docId);
            req.setAttribute("role", docRole.getType());
        }

        return forwardingDocDetail(req, docId);
    }

    private void setCommonAttr(HttpServletRequest req, Doc doc, Long docId) {
        //작성자 및 부서 이름 정보 추가
        req.setAttribute("writer", empDao.getEmpById(doc.getDocWriter()));
        req.setAttribute("dept", deptDao.getDeptByEmpId(doc.getDocWriter()));

        //결재자 정보 추가
        List<Sign> signList = docDao.getSignListByDocId(docId);
        req.setAttribute("signList", signList);

        //결재자 이름 표시를 위한 Map 추가
        HashMap<String, String> nameMap = new HashMap<>();
        for (Sign sign : signList) {
            nameMap.put(sign.getEmpId(), empDao.getEmpById(sign.getEmpId()).getEname());
        }
        req.setAttribute("nameMap", nameMap);
    }

    /*
        1. 내가 작성자 2. 같은 부서원이 기안 1. 내가 결재자에 포함된 경우
        조회가능(2번이 중족되면 1번은 자동충족이므로 2번, 3번만 확인)
     */
    private boolean isValidToAccessDoc(String loginEmpId, Long docId) {
        Doc doc = docDao.getGeneralDocByDocId(docId);
        List<Sign> signList = docDao.getSignListByDocId(docId);
        Long loginDept = empDao.getEmpById(loginEmpId).getDeptId();
        Long writerDept = empDao.getEmpById(doc.getDocWriter()).getDeptId();

        return Objects.equals(loginDept, writerDept)
                || signList.stream().anyMatch(s -> s.getEmpId().equals(loginEmpId));
    }

    private DocRole checkRole(String loginEmpId, Long docId) {
        Doc doc = docDao.getGeneralDocByDocId(docId);

        // 작성자일 경우 바로 설정 후 반환
        if (doc.getDocWriter().equals(loginEmpId)) {
            return DocRole.WRITER;
        }

        // 결재자 리스트에서 현재 결재 순번의 결재자인 경우 설정
        List<Sign> signList = docDao.getSignListByDocId(docId);
        for (Sign sign : signList) {
            if (sign.getSignTime() == null) {
                return sign.getEmpId().equals(loginEmpId) ? DocRole.SIGNER : DocRole.VIEWER;
            }
        }

        // 그 외의 경우 단순 조회자로 설정
        return DocRole.VIEWER;
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

    private ActionForward forwardingDocDetail(HttpServletRequest req, Long docId) {
        Doc doc = getDetailedDocById(docId);

        if (doc instanceof DocBuy docBuy) {
            req.setAttribute("docBuy", docBuy);
            return new ActionForward(false, "/WEB-INF/views/doc/doc_buy_read.jsp");
        }

        if (doc instanceof DocTrip docTrip) {
            req.setAttribute("docTrip", docTrip);
            return new ActionForward(false, "/WEB-INF/views/doc/doc_trip_read.jsp");
        }

        if (doc instanceof DocVacation docVacation) {
            req.setAttribute("docVacation", docVacation);
            return new ActionForward(false, "/WEB-INF/views/doc/doc_vacation_read.jsp");
        }
        //일반 문서 페이지로 포워딩
        req.setAttribute("doc", doc);
        return new ActionForward(false, "/WEB-INF/views/doc/doc_general_read.jsp");
    }
}
