package com.hta2405.unite.action.postcomments;

import java.io.IOException;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.PostCommentDao;
import com.hta2405.unite.dto.PostComment;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PostCommentsAddAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		PostCommentDao postCommentDao = new PostCommentDao();
		
		PostComment postCommentData = new PostComment();
		postCommentData.setPostCommentWriter(req.getParameter("empId"));
		postCommentData.setPostCommentContent(req.getParameter("postCommentContent"));
		System.out.println("content="+postCommentData.getPostCommentContent());
		
		postCommentData.setPostCommentReLev(Long.parseLong(req.getParameter("postCommentReLev")));
		postCommentData.setPostId(Long.parseLong(req.getParameter("postId")));
		postCommentData.setPostCommentReSeq(Long.parseLong(req.getParameter("postCommentReSeq")));
		
		int ok = postCommentDao.commentsInsert(postCommentData);
		resp.getWriter().print(ok);
		return null;
	}
}
