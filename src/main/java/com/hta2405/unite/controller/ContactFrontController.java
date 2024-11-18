package com.hta2405.unite.controller;

import com.hta2405.unite.action.contact.AddressBookAction;
import com.hta2405.unite.action.contact.AllEmployeesByJobAction;
import com.hta2405.unite.action.contact.DeptEmployeesByJobAction;
import com.hta2405.unite.action.contact.AllEmployeesByNameAction;
import com.hta2405.unite.action.contact.SearchAction;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/contact/*")
public class ContactFrontController extends AbstractFrontController {
	@Override
	public void init() throws ServletException {
		actionMap.put("/addressbook", new AddressBookAction());
		actionMap.put("/deptbyjob", new DeptEmployeesByJobAction());
		actionMap.put("/allemployeesbyname", new AllEmployeesByNameAction());
		actionMap.put("/allemployeesbyjob", new AllEmployeesByJobAction());
		actionMap.put("/search", new SearchAction());
	}
}