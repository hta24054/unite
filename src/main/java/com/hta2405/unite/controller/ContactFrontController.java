package com.hta2405.unite.controller;

import com.hta2405.unite.action.contact.AddressBookAction;
import com.hta2405.unite.action.contact.ContactViewAction;
import com.hta2405.unite.action.contact.AllEmployeesAction;
import com.hta2405.unite.action.contact.SearchAction;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/contact/*")
public class ContactFrontController extends AbstractFrontController {
@Override
	public void init() throws ServletException {
		actionMap.put("/addressbook", new AddressBookAction());
		actionMap.put("/view", new ContactViewAction());
		actionMap.put("/allEmployees", new AllEmployeesAction());
		actionMap.put("/search", new SearchAction());
	}
}