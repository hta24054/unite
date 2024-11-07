package com.hta2405.unite.controller;

import com.hta2405.unite.action.attend.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/attend/*")
public class AttendFrontController extends AbstractFrontController {

    @Override
    public void init() throws ServletException {
        actionMap.put("/attendIn", new AttendInAction());
        actionMap.put("/attendOut", new AttendOutAction());
        actionMap.put("/attendInfo", new AttendInfoAction());
        actionMap.put("/my", new AttendMyDetailAction());
        actionMap.put("/empList", new AttendEmpListAction());
        actionMap.put("/emp", new AttendEmpDetailAction());
        actionMap.put("/vacation/my", new AttendVacationDetailAction());
        actionMap.put("/vacation/empList", new AttendVacationEmpListAction());
        actionMap.put("/vacation/emp", new AttendVacationEmpDetailAction());
    }
}