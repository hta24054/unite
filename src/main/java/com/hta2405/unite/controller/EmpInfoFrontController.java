package com.hta2405.unite.controller;

import com.hta2405.unite.action.empInfo.EmpViewAction;
import com.hta2405.unite.action.empInfo.EmpDetailViewAction;
import com.hta2405.unite.action.empInfo.EmpInfoUpdateAction;
import com.hta2405.unite.action.empInfo.EmpViewDeptAction;
import com.hta2405.unite.action.empInfo.EmpViewOtherDeptInfoAction;
import com.hta2405.unite.action.empInfo.EmpViewOtherDeptAction;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/empInfo/*")
public class EmpInfoFrontController extends AbstractFrontController {

	@Override
	public void init() throws ServletException {
        actionMap.put("/view", EmpViewAction::new);
        actionMap.put("/update", EmpInfoUpdateAction::new);
        actionMap.put("/viewdept", EmpViewDeptAction::new);
        actionMap.put("/viewotherdeptinfo", EmpViewOtherDeptInfoAction::new);
        actionMap.put("/viewotherdept", EmpViewOtherDeptAction::new);
        actionMap.put("/detail", EmpDetailViewAction::new); // 추가된 부분
	}
}
