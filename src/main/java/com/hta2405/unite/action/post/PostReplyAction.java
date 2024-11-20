package com.hta2405.unite.action.post;

import java.io.IOException;
import java.util.List;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.BoardDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PostReplyAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		ActionForward forward = new ActionForward();
		BoardDao boardDao = new BoardDao();
		
		//파라미터로 전달받은 수정할 글 번호를 num변수에 저장합니다.
		Long num = Long.parseLong(req.getParameter("num"));
		
		//글 번호 num에 해당하는 내용을 가져와서 boarddata객체에 저장합니다.
		List<Object> list = boardDao.getDetail(num); //post emp postFileList
		
		//글 내용이 없는 경우
		if(list == null) {
			System.out.println("원본글이 존재하지 않습니다.");
			req.setAttribute("message", "원본글이 존재하지 않습니다.");
			forward.setPath("/WEB-INF/views/board/boardHome.jsp");
		}else {
			System.out.println("답변 페이지 이동 완료");
			
			//board의 boardName2를 가져오기 위함
			req.setAttribute("boardMap", boardDao.getIdToboardName2Map());
			
			//답변 폼 페이지로 이동할 때 원문 글 내용을 보여주기 위해 
			//request 객체에 저장합니다.
			req.setAttribute("postData", list.get(0));// post 데이터만 보냄
			
			//글 답변 페이지 경로 지정합니다.
			forward.setPath("/WEB-INF/views/post/postReply.jsp");
		}
		forward.setRedirect(false);
		return forward;
	}
}
