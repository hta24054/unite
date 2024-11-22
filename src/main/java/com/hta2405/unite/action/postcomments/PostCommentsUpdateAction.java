package com.hta2405.unite.action.postcomments;

import java.io.IOException;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.PostCommentDao;
import com.hta2405.unite.dto.PostComment;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PostCommentsUpdateAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PostCommentDao dao = new PostCommentDao();
		PostComment postCommentData = new PostComment();
		postCommentData.setPostCommentContent(req.getParameter("postCommentContent"));
		System.out.println("content="+postCommentData.getPostCommentContent());
		
		postCommentData.setCommentId(Long.parseLong(req.getParameter("commentId")));
		
		int ok = dao.commentsUpdate(postCommentData);
		resp.getWriter().print(ok);
		return null;
	}
}
