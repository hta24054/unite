package com.hta2405.unite.action.postcomments;

import java.io.IOException;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.PostCommentDao;
import com.hta2405.unite.dto.PostComment;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PostCommentsReplyAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PostCommentDao postCommentDao = new PostCommentDao();
		PostComment postCommentData = new PostComment();
		
		postCommentData.setPostId(Long.parseLong(req.getParameter("postId")));
		postCommentData.setPostCommentWriter(req.getParameter("empId"));
		postCommentData.setPostCommentContent(req.getParameter("postCommentContent"));
		postCommentData.setPostCommentReLev(Long.parseLong(req.getParameter("postCommentReLev")));
		postCommentData.setPostCommentReSeq(Long.parseLong(req.getParameter("postCommentReSeq")));
		postCommentData.setPostCommentReRef(Long.parseLong(req.getParameter("postCommentReRef")));
		
		int ok = postCommentDao.commentsReply(postCommentData);
		resp.getWriter().print(ok);
		return null;
	}
}
