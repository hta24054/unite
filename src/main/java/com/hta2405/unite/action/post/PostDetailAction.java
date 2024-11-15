package com.hta2405.unite.action.post;

import java.io.IOException;
import java.util.List;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.BoardDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class PostDetailAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		BoardDao boardDao = new BoardDao();
		
		//글번호 파라미터 값을 num변수에 저장합니다.
		int num = Integer.parseInt(request.getParameter("num"));
		
		// boards/list 에서 boards/detail로 이동 후 sessionReferer 값 확인
		HttpSession session = request.getSession();
		String sessionReferer = (String) session.getAttribute("referer");
		
		if(sessionReferer != null && sessionReferer.equals("list")) {
			//특정 주소로부터의 이동을 확인하는 데 사용할 수 있는 정보는 request Header의 "referer"에 있습니다.
			String headerReferer = request.getHeader("referer");
			System.out.println("headerReferer="+headerReferer);
			
			
			
			if(headerReferer != null && headerReferer.contains("board/home")) {// board/home 임시
				//내용을 확인할 글의 조회수를 증가시킵니다.
				boardDao.setReadCountUpdate(num);
				System.out.println("count 증가");
			}
			session.removeAttribute("referer");
		}
		
		//글 내용을 DAO에서 읽은 후 얻은 결과를 list에 저장합니다.
		List<Object> list = boardDao.getDetail(num);
		
		System.out.println(list);
		
		ActionForward forward = new ActionForward();
		//DAO에서 글의 내용을 읽지 못했을 경우 null을 반환합니다.
		if(list == null) {
			System.out.println("상세보기 실패");
			request.setAttribute("message", "데이터를 읽지 못했습니다.");
			forward.setPath("/WEB-INF/views/error/forbidden.jsp");
		}else {
			System.out.println("상세보기 성공");
			// boarddata 객체를 request 객체에 저장합니다.
			request.setAttribute("postList", list); // post, postFile, postFile...
			forward.setPath("/WEB-INF/views/post/boardView.jsp");//글 내용 보기 페이지로 이동하기 위해 경로를 설정
		}
		forward.setRedirect(false);
		return forward;
	}

}
