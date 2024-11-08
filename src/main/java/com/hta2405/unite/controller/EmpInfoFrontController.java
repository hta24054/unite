package com.hta2405.unite.controller;

import com.hta2405.unite.action.EmpInfoUpdateAction;
import com.hta2405.unite.action.EmpViewAction;
import com.hta2405.unite.action.EmpViewDeptAction;
import com.hta2405.unite.action.EmpViewOtherDeptAction;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/empInfo/*")
public class EmpInfoFrontController extends AbstractFrontController {

    @Override
    public void init() throws ServletException {
        actionMap.put("/view", new EmpViewAction());
        actionMap.put("/update", new EmpInfoUpdateAction());
        actionMap.put("/viewdept", new EmpViewDeptAction());
        actionMap.put("/viewotherdept", new EmpViewOtherDeptAction());
    }
}
