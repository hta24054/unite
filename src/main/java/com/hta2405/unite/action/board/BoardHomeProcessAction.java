package com.hta2405.unite.action.board;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.BoardDao;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.util.LocalDateAdapter;
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

		//emp를 위해 어댑터 등록
		Gson gson2 = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
		
		
		//dao를 사용해 이름과 이메일이 같은지 비교
		BoardDao dao = new BoardDao();
		
		Long deptId = new EmpDao().getEmpById((String) req.getSession().getAttribute("id")).getDeptId();
		ArrayList<Object> list = dao.getBoardListAll(deptId);//board post emp가 들어있음
		
		JsonObject object = null;
		
		if(list == null || list.isEmpty()) {
			System.out.println("게시글이 없습니다.");
			resp.setContentType("application/json;charset=utf-8");
			resp.getWriter().print(object);
			return null;
		} else {
			System.out.println("게시글 가져오기 성공");
			
			System.out.println("list="+list);
			
			object = new JsonObject();
			Object boardList = list.get(0);
			Object postsList = list.get(1);
			Object empList = list.get(2);
			
			
			
			object.add("boardList", gson.toJsonTree(boardList));
			object.add("postList", gson.toJsonTree(postsList));
			object.add("empList",gson2.toJsonTree(empList));
			
			//emp의 ename을 구하기 위한 hashMap
			object.add("empMap", gson.toJsonTree(new EmpDao().getIdToENameMap()));
			
			resp.setContentType("application/json;charset=utf-8");
			resp.getWriter().print(object);
			System.out.println(object.toString());
			return null;
		}
		
	}
}
