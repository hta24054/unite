package com.hta2405.unite.controller;

import com.hta2405.unite.action.board.BoardHomeAction;
import com.hta2405.unite.action.board.BoardHomeProcessAction;
import com.hta2405.unite.action.board.BoardListAction;
import com.hta2405.unite.action.post.PostAddAction;
import com.hta2405.unite.action.post.PostCommentsListAction;
import com.hta2405.unite.action.post.PostDetailAction;
import com.hta2405.unite.action.post.PostModifyAction;
import com.hta2405.unite.action.post.PostModifyProcessAction;
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
        actionMap.put("/boardList", new BoardListAction());
        actionMap.put("/post/detail", new PostDetailAction());
        actionMap.put("/post/postWrite", new PostWriteAction());//게시글 쓰기
        actionMap.put("/post/add", new PostAddAction());//게시글 추가 액션
        actionMap.put("/post/modify", new PostModifyAction());//게시글 수정 폼 조회 액션
        actionMap.put("/post/modifyProcess", new PostModifyProcessAction());//게시글 수정 액션
        actionMap.put("/post/comments/list", new PostCommentsListAction());//게시글의 댓글 리스트
    }
}
