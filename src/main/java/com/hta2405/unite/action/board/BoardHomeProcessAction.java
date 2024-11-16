package com.hta2405.unite.action.board;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.BoardDao;
import com.hta2405.unite.dto.Board;
import com.hta2405.unite.dto.Post;
import com.hta2405.unite.util.LocalDateTimeAdapter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BoardHomeProcessAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		//Gson 인스턴스 생성할 때 어댑터 등록
		Gson gson = new GsonBuilder()
		        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
		        .create();

		//dao를 사용해 이름과 이메일이 같은지 비교
		BoardDao dao = new BoardDao();
		
		ArrayList<Object> list = dao.getBoardListAll();
		
		JsonObject object = null;
		
		if(list == null || list.isEmpty()) {
			System.out.println("게시글이 없습니다.");
			resp.setContentType("application/json;charset=utf-8");
			resp.getWriter().print(object);
			return null;
		} else {
			System.out.println("게시글 가져오기 성공");
			object = new JsonObject();
			Object boards = list.get(0);
			Object posts = list.get(1);

			System.out.println("list="+list);
			System.out.println("boards="+boards);

			
			JsonElement je1 = gson.toJsonTree(boards);
			System.out.println("boards="+je1.toString());
			
			JsonElement je2 = gson.toJsonTree(posts);
			System.out.println("posts="+je2.toString());
			
			object.add("boards", je1);
			object.add("posts", je2);
			resp.setContentType("application/json;charset=utf-8");
			resp.getWriter().print(object);
			System.out.println(object.toString());
			return null;
		}
		
	}

}
