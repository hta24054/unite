package com.hta2405.unite.controller;

import com.hta2405.unite.action.board.BoardHomeAction;
import com.hta2405.unite.action.board.BoardHomeProcessAction;
import com.hta2405.unite.action.board.BoardListAction;
import com.hta2405.unite.action.post.PostAddAction;
import com.hta2405.unite.action.post.PostWriteAction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 5,   // 메모리 내 파일 크기 제한 (5MB)
    maxFileSize = 1024 * 1024 * 10,        // 파일 하나의 최대 크기 (10MB)
    maxRequestSize = 1024 * 1024 * 50      // 요청 전체 크기 (50MB)
)
@WebServlet("/board/*")
public class BoardFrontController extends AbstractFrontController {

    @Override
    public void init() throws ServletException {
        actionMap.put("/home", new BoardHomeAction());//게시판 홈
        actionMap.put("/homeProcess", new BoardHomeProcessAction());//게시판 홈
        actionMap.put("/boardWrite", new PostWriteAction());
        for(int num=1;num<=30;num++) {//게시판id 개수만큼 반복(임의)
            actionMap.put("/list"+num, new BoardListAction(num));
        }
        
        actionMap.put("/post/postWrite", new PostWriteAction());//게시글 쓰기
        actionMap.put("/post/add", new PostAddAction());//게시글 추가 액션
    }
}
