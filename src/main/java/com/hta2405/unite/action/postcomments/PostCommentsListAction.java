package com.hta2405.unite.action.postcomments;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dao.PostCommentDao;
import com.hta2405.unite.util.LocalDateTimeAdapter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PostCommentsListAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//Gson 인스턴스 생성할 때 어댑터 등록
		Gson gson = new GsonBuilder()
		        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
		        .create();
		
		
		PostCommentDao commentDao = new PostCommentDao();
		
		//{"postId" : $("#postId").val(), state:state}, //state값이 1=>등록순, 2=>최신순
		Long postId = Long.parseLong(req.getParameter("postId"));
		System.out.println(postId);
		int state = Integer.parseInt(req.getParameter("state"));
		int listCount = commentDao.getListCount(postId);

		JsonObject object = new JsonObject();
		object.addProperty("listCount", listCount);
		
		JsonArray jarray = commentDao.getCommentList(postId, state);
		JsonElement je = new Gson().toJsonTree(jarray);
		object.add("postCommentList",je);
		
		//postCommentWriter로 이름을 구하기 위함
		JsonElement jeEmpMap = gson.toJsonTree(new EmpDao().getIdToENameMap());
		object.add("empMap",jeEmpMap);
		
		resp.setContentType("application/json;charset=utf-8");
		PrintWriter out =resp.getWriter();
		out.print(object.toString());
		System.out.println(object.toString());
		return null;
	}

}
