package com.hta2405.unite.action.post;

import java.io.IOException;
import java.util.List;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.BoardDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PostModifyAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ActionForward forward = new ActionForward();
		BoardDao boardDao = new BoardDao();
		
		//파라미터로 전달받은 수정할 글 번호를 postId변수에 저장합니다.
		Long postId = Long.parseLong(req.getParameter("id"));
		
		//글 내용을 불러와서 list에 저장
		List<Object> list = boardDao.getDetail(postId);
		
		if(list == null) {//글 내용 불러오기 실패한 경우
			System.out.println("(수정)상세보기 실패");
			req.setAttribute("message", "게시판 수정 상세보기 실패입니다.");
			forward.setPath("/WEB-INF/views/board/boardHome.jsp");
		}else {
			System.out.println("(수정)상세보기 성공");
			
			req.setAttribute("boardName2", req.getParameter("boardName2")); //게시판 메뉴 설정하기 위함
			req.setAttribute("list", list); //list에는 post, emp, postFileList 존재
			
			//글 수정 폼 페이지로 이동하기 위해 경로를 설정합니다.
			forward.setPath("/WEB-INF/views/post/postModify.jsp");
		}
		forward.setRedirect(false);
		return forward;
	}

}
