package com.hta2405.unite.controller;

import com.hta2405.unite.action.doc.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;

@MultipartConfig(
        maxFileSize = 1024 * 1024 * 5      // 5MB
)
@WebServlet("/doc/*")
public class DocFrontController extends AbstractFrontController {
    @Override
    public void init() throws ServletException {
        actionMap.put("/waiting", new DocWaitingListAction());
        actionMap.put("/general", new DocGeneralWriteAction());
        actionMap.put("/general_write", new DocGeneralWriteAndEditProcessAction());
        actionMap.put("/general_edit", new DocGeneralWriteAndEditProcessAction());
        actionMap.put("/vacation", new DocVacationWriteAction());
        actionMap.put("/vacation_write", new DocVacationWriteAndEditProcessAction());
        actionMap.put("/vacation_edit", new DocVacationWriteAndEditProcessAction());
        actionMap.put("/trip", new DocTripWriteAction());
        actionMap.put("/trip_write", new DocTripWriteAndEditProcessAction());
        actionMap.put("/trip_edit", new DocTripWriteAndEditProcessAction());
        actionMap.put("/buy", new DocBuyWriteAction());
        actionMap.put("/buy_write", new DocBuyWriteAndEditProcessAction());
        actionMap.put("/buy_edit", new DocBuyWriteAndEditProcessAction());
        actionMap.put("/my-approved", new DocMyApprovedListAction());
        actionMap.put("/read", new DocReadAction());
        actionMap.put("/in-progress", new DocInProgressAction());
        actionMap.put("/countVacation", new DocCountVacationAction());
        actionMap.put("/list/dept", new DocDeptListAction());
        actionMap.put("/list/sign", new DocSignListAction());
        actionMap.put("/download", new DocVacationFileDownloadAction());
        actionMap.put("/sign", new DocSignAction());
        actionMap.put("/revoke", new DocRevokeAction());
        actionMap.put("/delete", new DocDeleteAction());
        actionMap.put("/edit", new DocEditAction());
    }
}
