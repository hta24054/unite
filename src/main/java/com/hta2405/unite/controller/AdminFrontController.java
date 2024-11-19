package com.hta2405.unite.controller;

import com.hta2405.unite.action.admin.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/admin/*")
public class AdminFrontController extends AbstractFrontController {

    @Override
    public void init() throws ServletException {
        actionMap.put("/holiday", AdminHolidayAction::new);
        actionMap.put("/holiday/get", AdminHolidayGetAction::new);
        actionMap.put("/holiday/insert", AdminHolidayInsertAction::new);
        actionMap.put("/holiday/delete", AdminHolidayDeleteAction::new);
        actionMap.put("/holiday/api", AdminHolidayApiAction::new);
        actionMap.put("/holiday/weekend", AdminHolidayWeekendAction::new);
        actionMap.put("/resource", AdminResourceAction::new);
        actionMap.put("/resource/add", AdminResourceAddAction::new);
        actionMap.put("/resource/edit", AdminResourceEditAction::new);
        actionMap.put("/resource/delete", AdminResourceDeleteAction::new);
        actionMap.put("/notice", AdminNoticeAction::new);
        actionMap.put("/notice/insert", AdminNoticeAddAction::new);
        actionMap.put("/notice/update", AdminNoticeEditAction::new);
        actionMap.put("/notice/delete", AdminNoticeDeleteAction::new);
        actionMap.put("/emp-manage", AdminEmpManageAction::new);
        actionMap.put("/emp-manage/fire", AdminEmpFireAction::new);
    }
}