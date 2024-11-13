package com.hta2405.unite.controller;

import com.hta2405.unite.action.doc.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/doc/*")
public class DocFrontController extends AbstractFrontController {
    @Override
    public void init() throws ServletException {
        actionMap.put("/waiting", new DocWaitingListAction());
        actionMap.put("/general", new DocGeneralWriteAction());
        actionMap.put("/general_process", new DocGeneralWriteProcessAction());
        actionMap.put("/vacation", new DocVacationWriteAction());
        actionMap.put("/trip", new DocTripWriteAction());
        actionMap.put("/buy", new DocBuyWriteAction());
        actionMap.put("/in-progress", new DocInProgressListAction());
        actionMap.put("/dept", new DocDeptListAction());
        actionMap.put("/my-approved", new DocMyApprovedListAction());
        actionMap.put("/detail", new DocReadAction());
    }
}
