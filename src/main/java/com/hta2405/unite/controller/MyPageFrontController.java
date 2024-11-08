package com.hta2405.unite.controller;

import com.hta2405.unite.action.mypage.MyPagePasswordChangeAction;
import com.hta2405.unite.action.mypage.MyPagePasswordChangeProcessAction;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/mypage/*")
public class MyPageFrontController extends AbstractFrontController {
    @Override
    public void init() throws ServletException {
        actionMap.put("/password", new MyPagePasswordChangeAction());
        actionMap.put("/password/process", new MyPagePasswordChangeProcessAction());
    }
}
