package com.hta2405.unite.action.board;

import java.io.IOException;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BoardListAction implements Action {
	private int num = 0;//게시판 id
	
	public BoardListAction() {
		num = 0;//초기화
	}
	
	public BoardListAction(int num) {
		this.num = num;
	}

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ActionForward forward = new ActionForward();
		
		//num(게시판id)을 조건으로 board 테이블에서 각 게시판 조회
		
		req.setAttribute("num", num);//게시판id
		
		forward.setPath("/WEB-INF/views/board/boardList.jsp"); //각 게시판을 식별가능한 데이터(게시판명,게시글dto..)를 같이 보냄
		forward.setRedirect(false);
		return forward;
	}
}