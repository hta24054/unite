package com.hta2405.unite.controller;

import com.hta2405.unite.action.HomeAction;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/home/*")
public class HomeFrontController extends AbstractFrontController {
    @Override
    public void init(ServletConfig config) throws ServletException {
        actionMap.put("/", HomeAction::new);
    }
}
