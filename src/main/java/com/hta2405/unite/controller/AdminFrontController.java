package com.hta2405.unite.controller;

import com.hta2405.unite.action.admin.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/admin/*")
public class AdminFrontController extends AbstractFrontController {

    @Override
    public void init() throws ServletException {
        actionMap.put("/holiday", new AdminHolidayAction());
        actionMap.put("/holiday/get", new AdminHolidayGetAction());
        actionMap.put("/holiday/insert", new AdminHolidayInsertAction());
        actionMap.put("/holiday/delete", new AdminHolidayDeleteAction());
        actionMap.put("/holiday/api", new AdminHolidayApiAction());
        actionMap.put("/holiday/weekend", new AdminHolidayWeekendAction());
        actionMap.put("/popup", new AdminPopupAction());
        actionMap.put("/resource", new AdminResourceAction());
        actionMap.put("/resource/add", new AdminResourceAddAction());
        actionMap.put("/resource/edit", new AdminResourceEditAction());
        actionMap.put("/resource/delete", new AdminResourceDeleteAction());
        actionMap.put("/emp-manage", new AdminEmpManageAction());
    }
}