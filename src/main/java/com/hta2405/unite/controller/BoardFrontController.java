package com.hta2405.unite.controller;

import com.hta2405.unite.action.BoardHomeAction;
import com.hta2405.unite.action.BoardListAction;
import com.hta2405.unite.action.BoardWriteAction;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/board/*")
public class BoardFrontController extends AbstractFrontController {

    @Override
    public void init() throws ServletException {
        actionMap.put("", new BoardHomeAction());//게시판 홈
        actionMap.put("/write", new BoardWriteAction());
        for (int num = 1; num <= 30; num++) {//게시판id 개수만큼 반복(임의)
            actionMap.put("/list" + num, new BoardListAction(num));
        }
    }
}
