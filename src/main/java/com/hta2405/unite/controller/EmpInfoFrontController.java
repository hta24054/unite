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
		actionMap.put("/view", new EmpViewAction());
		actionMap.put("/update", new EmpInfoUpdateAction());
		actionMap.put("/viewdept", new EmpViewDeptAction());
		actionMap.put("/viewotherdeptinfo", new EmpViewOtherDeptInfoAction());
		actionMap.put("/viewotherdept", new EmpViewOtherDeptAction());
		actionMap.put("/detail", new EmpDetailViewAction()); // 추가된 부분
	}
}
