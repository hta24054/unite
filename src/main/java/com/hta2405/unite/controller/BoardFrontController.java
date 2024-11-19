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
        actionMap.put("/home", BoardHomeAction::new);//게시판 홈
        actionMap.put("/homeProcess", BoardHomeProcessAction::new);//게시판 홈
        actionMap.put("/boardList", BoardListAction::new);
        actionMap.put("/post/detail", PostDetailAction::new);
        actionMap.put("/post/postWrite", PostWriteAction::new);//게시글 쓰기
        actionMap.put("/post/add", PostAddAction::new);//게시글 추가 액션
        actionMap.put("/post/modify", PostModifyAction::new);//게시글 수정 폼 조회 액션
        actionMap.put("/post/modifyProcess", PostModifyProcessAction::new);//게시글 수정 액션
        actionMap.put("/post/comments/list", PostCommentsListAction::new);//게시글의 댓글 리스트
    }
}
