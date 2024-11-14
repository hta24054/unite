package com.hta2405.unite.controller;

import com.hta2405.unite.action.contact.AddressBookAction;
import com.hta2405.unite.action.contact.ContactViewAction;
import com.hta2405.unite.action.contact.ContactViewDeptAction;
import com.hta2405.unite.action.contact.ContactViewNameAction;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/contact/*")
public class ContactFrontController extends AbstractFrontController {

    @Override
    public void init() throws ServletException {
        actionMap.put("/view", new ContactViewAction());
        actionMap.put("/viewdept", new ContactViewDeptAction());
        actionMap.put("/viewname", new ContactViewNameAction());
        actionMap.put("/addressbook", new AddressBookAction());
    }
}
