package com.hta2405.unite.controller;

import com.hta2405.unite.action.attend.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/attend/*")
public class AttendFrontController extends AbstractFrontController {

    @Override
    public void init() throws ServletException {
        actionMap.put("/attendIn", AttendInAction::new);
        actionMap.put("/attendOut", AttendOutAction::new);
        actionMap.put("/attendInfo", AttendInfoAction::new);
        actionMap.put("/my", AttendMyDetailAction::new);
        actionMap.put("/empList", AttendEmpListAction::new);
        actionMap.put("/emp", AttendEmpDetailAction::new);
        actionMap.put("/vacation/my", AttendMyVacationDetailAction::new);
        actionMap.put("/vacation/empList", AttendVacationEmpListAction::new);
        actionMap.put("/vacation/emp", AttendVacationEmpDetailAction::new);
    }
}