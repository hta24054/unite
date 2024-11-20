package com.hta2405.unite.action.post;

import java.io.IOException;
import java.util.List;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.BoardDao;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Post;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PostDetailAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		BoardDao boardDao = new BoardDao();
		
		//글번호 파라미터 값을 num변수에 저장합니다.
		Long postId = Long.parseLong(req.getParameter("num"));
		
		//내용을 확인할 글의 조회수를 증가시킵니다.
		boardDao.setReadCountUpdate(postId);
		System.out.println("count 증가");
		
		System.out.println("postId="+postId);
		//글 내용을 DAO에서 읽은 후 얻은 결과를 list에 저장합니다.
		
		List<Object> list = boardDao.getDetail(postId);
		
		System.out.println("list="+list);
		
		ActionForward forward = new ActionForward();
		//DAO에서 글의 내용을 읽지 못했을 경우 null을 반환합니다.
		if(list == null) {
			System.out.println("상세보기 실패");
			req.setAttribute("message", "데이터를 읽지 못했습니다.");
			forward.setPath("/WEB-INF/views/error/forbidden.jsp");
		}else {
			System.out.println("상세보기 성공");
			
			//emp의 ename을 가져오기 위함
			req.setAttribute("empMap", new EmpDao().getIdToENameMap());
			
			//board의 boardName2를 가져오기 위함
			req.setAttribute("boardMap", boardDao.getIdToboardName2Map());
			
			// boarddata 객체를 request 객체에 저장합니다.
			req.setAttribute("postDataAndFile", list); // post, emp, postFileList
			forward.setPath("/WEB-INF/views/post/boardView.jsp");//글 내용 보기 페이지로 이동하기 위해 경로를 설정
		}
		forward.setRedirect(false);
		return forward;
	}

}
