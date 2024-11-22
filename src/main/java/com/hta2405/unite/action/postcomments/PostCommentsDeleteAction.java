package com.hta2405.unite.action.postcomments;

import java.io.IOException;
import java.io.PrintWriter;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.PostCommentDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PostCommentsDeleteAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Long commentId = Long.parseLong(req.getParameter("commentId"));
		
		PostCommentDao dao = new PostCommentDao();
		
		int result = dao.commentsDelete(commentId);
		PrintWriter out = resp.getWriter();
		out.print(result);
		return null;
	}

}
