package com.hta2405.unite.controller;

import com.hta2405.unite.action.board.BoardHomeAction;
import com.hta2405.unite.action.board.BoardHomeProcessAction;
import com.hta2405.unite.action.board.BoardListAction;
import com.hta2405.unite.action.board.BoardSearchAction;
import com.hta2405.unite.action.post.PostAddAction;
import com.hta2405.unite.action.post.PostDeleteAction;
import com.hta2405.unite.action.post.PostDetailAction;
import com.hta2405.unite.action.post.PostFileDownAction;
import com.hta2405.unite.action.post.PostModifyAction;
import com.hta2405.unite.action.post.PostModifyProcessAction;
import com.hta2405.unite.action.post.PostReplyAction;
import com.hta2405.unite.action.post.PostReplyProcessAction;
import com.hta2405.unite.action.post.PostWriteAction;
import com.hta2405.unite.action.postcomments.PostCommentsAddAction;
import com.hta2405.unite.action.postcomments.PostCommentsDeleteAction;
import com.hta2405.unite.action.postcomments.PostCommentsListAction;
import com.hta2405.unite.action.postcomments.PostCommentsReplyAction;
import com.hta2405.unite.action.postcomments.PostCommentsUpdateAction;

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
        actionMap.put("/post/delete", PostDeleteAction::new);//게시글 삭제 액션
        actionMap.put("/post/reply", PostReplyAction::new);//게시글 답글 액션
        actionMap.put("/post/replyProcess", PostReplyProcessAction::new);//게시글 답글 액션
        actionMap.put("/post/down", PostFileDownAction::new);//첨부파일 다운 액션
        
        actionMap.put("/comments/list", PostCommentsListAction::new);//게시글의 댓글 리스트
        actionMap.put("/comments/add", PostCommentsAddAction::new);//게시글의 댓글 추가 액션
        actionMap.put("/comments/delete", PostCommentsDeleteAction::new);//게시글의 댓글 삭제 액션
        actionMap.put("/comments/update", PostCommentsUpdateAction::new);//게시글의 댓글 수정 액션
        actionMap.put("/comments/reply", PostCommentsReplyAction::new);//게시글의 댓글 답글 액션
        
        actionMap.put("/search", BoardSearchAction::new);//게시글 검색 액션
    }
}
