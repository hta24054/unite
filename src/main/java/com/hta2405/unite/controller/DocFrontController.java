package com.hta2405.unite.controller;

import com.hta2405.unite.action.doc.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
        maxFileSize = 1024 * 1024 * 10,       // 10MB
        maxRequestSize = 1024 * 1024 * 50     // 50MB
)
@WebServlet("/doc/*")
public class DocFrontController extends AbstractFrontController {
    @Override
    public void init() throws ServletException {
        actionMap.put("/waiting", new DocWaitingListAction());
        actionMap.put("/general", new DocGeneralWriteAction());
        actionMap.put("/general_write", new DocGeneralWriteProcessAction());
        actionMap.put("/vacation", new DocVacationWriteAction());
        actionMap.put("/vacation_write",new DocVacationWriteProcessAction());
        actionMap.put("/trip", new DocTripWriteAction());
        actionMap.put("/trip_write", new DocTripWriteProcessAction());
        actionMap.put("/buy", new DocBuyWriteAction());
        actionMap.put("/buy_write", new DocBuyWriteProcessAction());
        actionMap.put("/my-approved", new DocMyApprovedListAction());
        actionMap.put("/read", new DocReadAction());
        actionMap.put("/in-progress", new DocInProgressAction());
        actionMap.put("/countVacation", new DocCountVacationAction());
        actionMap.put("/list/dept", new DocDeptListAction());
        actionMap.put("/list/sign", new DocSignListAction());
        actionMap.put("/download", new DocVacationFileDownloadAction());
        actionMap.put("/sign", new DocSignAction());
        actionMap.put("/reject", new DocRejectAction());
    }
}
