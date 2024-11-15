package com.hta2405.unite.action.project;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ProjectDAO;
import com.hta2405.unite.dto.ProjectInfo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//메인프로젝트
public class ProjectGetOngoingAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userid = (String) req.getSession().getAttribute("id");
		System.out.println("userid : " + userid);
		
    	
    	ProjectDAO projectDAO = new ProjectDAO();
    	
		// /boards/list에서 /boards/detail로 접속하는 경우에만 카운트 되도록 하기 위해 세션에 저장(새로고침으로 방문자수 x)
		req.getSession().setAttribute("referer", "list");
		
		//로그인 성공시 파라미터 page가 없다. 그래서 초기값 필요
		int page = 1; //보여줄 page
		int limit = 5; //한 페이지에 보여줄 게시판 목록 수
		
		if(req.getParameter("page") != null) page = Integer.parseInt(req.getParameter("page"));
		System.out.println("넘어온 페이지 = " + page);
		
		if(req.getParameter("limit") != null) limit = Integer.parseInt(req.getParameter("limit"));
		System.out.println("넘어온 limit = " + limit);
		
		
		//리스트를 받아옴
		List<ProjectInfo> ongoingList = projectDAO.getOngoingProjectsList(page, limit, userid);
		System.out.println(ongoingList );

		
		//총 리스트 수 받아옴
		int listcount = projectDAO.getOngoingCountList(userid);
		System.out.println("리스트 사이즈" + listcount);
		
		int maxpage = (listcount + limit -1) / limit;
		System.out.println("총 페이지 수 = " + maxpage);
		
		int startpage = ((page - 1) / 5) * 5 + 1;
		System.out.println("현재 페이지에 보여줄 시작 페이지 수 : " + startpage);
		
		//endpage : 현재 페이지 그룹에서 보여줄 마지막 페이지 수([10], [20], [30] 등..)
		int endpage = startpage + 5-1;


		if(endpage > maxpage) endpage = maxpage;
		
		System.out.println("현재 페이지에 보여줄 마지막 페이지 수 : " + endpage);
		String state = req.getParameter("state");
		if(state == null) {
			System.out.println("state == null");
			req.setAttribute("page", page); //현재 페이지 수 
			req.setAttribute("maxpage", maxpage); //최대 페이지 수 
			req.setAttribute("startpage", startpage); //현재 페이지에 표시할 첫 페이지 수
			req.setAttribute("endpage", endpage); //현재 페이지에 표시할 끝 페이지 수
			req.setAttribute("listcount", listcount); //총 글의 수 
			req.setAttribute("boardlist", ongoingList ); //해당 페이지의 글 목록을 갖고 있는 리스트
			req.setAttribute("limit", limit); 
			
			ActionForward forward = new ActionForward();
			forward.setPath("/WEB-INF/views/project/project_main.jsp"); 
			forward.setRedirect(false);
			return forward;
		}else {
			System.out.println("state = ajax");
			
			//위에서 request로 담았던 것을 JsonObject에 담는다
			JsonObject object = new JsonObject(); 
			object.addProperty("page", page); //{"page" : 변수 page의 값} 형식으로 저장
			object.addProperty("maxpage", maxpage);
			object.addProperty("startpage", startpage);
			object.addProperty("endpage", endpage);
			object.addProperty("listcount", listcount);
			object.addProperty("limit", limit);

			JsonElement je = new Gson().toJsonTree(ongoingList );
			System.out.println("boardlist = " + je.toString());
			object.add("boardlist", je);
			
			resp.setContentType("application/json; charset = utf-8");
			resp.getWriter().print(object);
			System.out.println(object.toString());
			return null;
		}
    }
}
