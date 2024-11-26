package com.hta2405.unite.action.post;

import java.io.IOException;
import java.io.PrintWriter;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.BoardDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PostDeleteAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		int postId = Integer.parseInt(req.getParameter("num"));
		
		BoardDao boardDao = new BoardDao();
		
		boolean result = boardDao.postDelete(postId);
		
		String message = "데이터를 삭제하지 못했습니다.";
		
		if(result) {
			message = "삭제 되었습니다.";
		}
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out= resp.getWriter();
		out.print("<script>");
		out.print("alert('"+ message +"');");
		out.print("location.href='../home';");
		out.print("</script>");
		out.close();
		return null;
	}
}
