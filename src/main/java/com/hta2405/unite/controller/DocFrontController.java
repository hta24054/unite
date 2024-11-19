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
        actionMap.put("/waiting", DocWaitingListAction::new);
        actionMap.put("/waiting-process", DocWaitingListProcessAction::new);
        actionMap.put("/general", DocGeneralWriteAction::new);
        actionMap.put("/general_write", DocGeneralWriteAndEditProcessAction::new);
        actionMap.put("/general_edit", DocGeneralWriteAndEditProcessAction::new);
        actionMap.put("/vacation", DocVacationWriteAction::new);
        actionMap.put("/vacation_write", DocVacationWriteAndEditProcessAction::new);
        actionMap.put("/vacation_edit", DocVacationWriteAndEditProcessAction::new);
        actionMap.put("/trip", DocTripWriteAction::new);
        actionMap.put("/trip_write", DocTripWriteAndEditProcessAction::new);
        actionMap.put("/trip_edit", DocTripWriteAndEditProcessAction::new);
        actionMap.put("/buy", DocBuyWriteAction::new);
        actionMap.put("/buy_write", DocBuyWriteAndEditProcessAction::new);
        actionMap.put("/buy_edit", DocBuyWriteAndEditProcessAction::new);
        actionMap.put("/my-approved", DocMyApprovedListAction::new);
        actionMap.put("/read", DocReadAction::new);
        actionMap.put("/in-progress", DocInProgressAction::new);
        actionMap.put("/countVacation", DocCountVacationAction::new);
        actionMap.put("/list/dept", DocDeptListAction::new);
        actionMap.put("/list/sign", DocSignListAction::new);
        actionMap.put("/download", DocVacationFileDownloadAction::new);
        actionMap.put("/sign", DocSignAction::new);
        actionMap.put("/revoke", DocRevokeAction::new);
        actionMap.put("/delete", DocDeleteAction::new);
        actionMap.put("/edit", DocEditAction::new);
    }
}
