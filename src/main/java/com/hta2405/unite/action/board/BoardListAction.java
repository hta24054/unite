package com.hta2405.unite.action.board;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.BoardDao;
import com.hta2405.unite.dto.Post;
import com.hta2405.unite.util.LocalDateTimeAdapter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BoardListAction implements Action {
	
	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		//Gson 인스턴스 생성할 때 어댑터 등록
		Gson gson = new GsonBuilder()
		        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
		        .create();
		
		BoardDao boardDao = new BoardDao();
		
		String boardName2 = req.getParameter("boardName2");
		req.setAttribute("boardName2", boardName2);
		System.out.println("boardName2="+boardName2);
		
		Long boardId = boardDao.getBoardListByName2(boardName2).getBoardId();
		System.out.println("boardId="+boardId);
		
		List<Post> postList = new ArrayList<>();
		
		//로그인 성공시 파라미터 page가 없으므로 초기값 필요
		int page = 1;	//보여줄 page
		int limit = 10;	//한 페이지에 보여줄 게시판 목록의 수
		if(req.getParameter("page") != null) {
			page = Integer.parseInt(req.getParameter("page"));
		}
		System.out.println("넘어온 페이지 = " + page);
		
		
		if(req.getParameter("limit") != null) {
			limit = Integer.parseInt(req.getParameter("limit"));
		}
		System.out.println("넘어온 limit = " + limit);
		
		//총 리스트 수를 받아옵니다.
		int listCount = boardDao.getListCountByBoardId(boardId);
		
		//리스트를 받아옵니다.
		postList = boardDao.getPostListByBoardId(page,limit,boardId);
		
		/*
		 * 총 페이지 수
		  = (DB에 저장된 총 리스트의 수 + 한 페이지에서 보여주는 리스트의 수 - 1)/한 페이지에서 보여주는 리스트의 수
		 * 
		 * 예를 들어 한 페이지에서 보여주는 리스트의 수가 10개인 경우
		 * 예1) DB에 저장된 총 리스트의 수가 0이면 총 페이지수는 0페이지
		 * 예2) DB에 저장된 총 리스트의 수가 ( 1~10)이면 총 페이지수는 1페이지
		 * 예3) DB에 저장된 총 리스트의 수가 (11~20)이면 총 페이지수는 2페이지
		 * 예4) DB에 저장된 총 리스트의 수가 (21~30)이면 총 페이지수는 3페이지
		 */
		int maxPage = (listCount + limit -1)/limit;
		System.out.println("총 페이지수 = "+maxPage);
		
		/*
		 * startpage : 현재 페이지 그룹에서 맨 처음에 표시될 페이지 수를 의미합니다.
		 * ([1],[11],[21] 등...) 보여줄 페이지가 30개일 경우
		   [1][2][3]....[30]까지 다 표시하기에는 너무 많기 때문에 보통 한 페이지에는
		   10페이지 정도까지 이동할 수 있게 표시합니다.
		 * 예) 페이지 그룹이 아래와 같은 경우
		 	  [1][2][3][4][5][6][7][8][9][10]
		 * 예로 1~10페이지의 내용을 나타낼 때는 페이지 그룹은 [1][2][3]..[10]로 표시되고
		 * 11~20페이지의 내용을 나타낼때는 페이지 그룹은 [11][12][13]..[20]까지 표시됩니다.
		 */
		int startPage = ((page - 1)/10) * 10 + 1;
		System.out.println("현재 페이지에 보여줄 시작 페이지 수 : "+startPage);
		
		//endpage : 현재 페이지 그룹에서 보여줄 마지막 페이지 수([10], [20], [30] 등...)
		int endPage = startPage + 10 -1;
		
		/*
		 * 마지막 그룹의 마지막 페이지 값은 최대 페이지 값이다.
		 * 예로 마지막 페이지 그룹이 [21]~[30]인 경우
		 * 시작페이지는 21(startpage=21)와 마지막페이지는 30(endpage=30) 이지만
		 * 최대 페이지(maxpage)가 25라면 [21]~[25]까지만 표시되도록 합니다.
		 */
		if(endPage > maxPage) {
			endPage = maxPage;
		}
		
		System.out.println("현재 페이지에 보여줄 마지막 페이지 수 : "+endPage);
		String state = req.getParameter("state");
		
		if(state == null) {
			System.out.println("state==null");
			req.setAttribute("page", page); //현재 페이지 수
			req.setAttribute("maxPage", maxPage);//최대 페이지 수
			
			// 현재 페이지에 표시할 첫 페이지 수
			req.setAttribute("startPage", startPage);
			
			// 현재 페이지에 표시할 끝 페이지 수
			req.setAttribute("endPage", endPage);
			
			req.setAttribute("listCount", listCount);
			
			// 해당 페이지에 글 목록을 갖고 있는 리스트
			req.setAttribute("postList", postList);
			
			req.setAttribute("limit", limit);
			
			ActionForward forward = new ActionForward();
			forward.setRedirect(false);
			
			// 글 목록 페이지로 이동하기 위해 경로를 설정합니다.
			forward.setPath("/WEB-INF/views/board/boardList.jsp");
			return forward;
		}else {
			System.out.println("state=ajax");
			
			//위에서 request로 담았던 것을 JsonObject에 담습니다.
			JsonObject object = new JsonObject();
			object.addProperty("page", page);//{"page": 변수 page의 값} 형식으로 저장
			object.addProperty("maxPage", maxPage);
			object.addProperty("startPage", startPage);
			object.addProperty("endPage", endPage);
			object.addProperty("listCount", listCount);
			object.addProperty("limit", limit);
			
			//JsonObject에 List 형식을 담을 수 있는 addProperty() 존재하지 않습니다.
			//void com.google.gson.JsonObject.add(String property, JsonElement value)  메서드를 통해서 저장합니다.
			//List형식을 JsonElement로 바꾸어 주어야 object에 저장할 수 있습니다.
			
			//List => JsonElement
			JsonElement je = gson.toJsonTree(postList);
			System.out.println("postList="+je.toString());
			object.add("postList", je);
			
			resp.setContentType("application/json;charset=utf-8");
			resp.getWriter().print(object);
			System.out.println(object.toString());
			return null;
		}//else end
		
	}
}