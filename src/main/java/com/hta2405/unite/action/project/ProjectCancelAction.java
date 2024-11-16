package com.hta2405.unite.action.project;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ProjectDAO;
import com.hta2405.unite.dto.ProjectComplete;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//프로젝트 취소
public class ProjectCancelAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String userid = (String) request.getSession().getAttribute("id");
		System.out.println("userid : " + userid);
		
    	
    	ProjectDAO projectDAO = new ProjectDAO();
    	
		// /boards/list에서 /boards/detail로 접속하는 경우에만 카운트 되도록 하기 위해 세션에 저장(새로고침으로 방문자수 x)
		request.getSession().setAttribute("referer", "list");
		
		//로그인 성공시 파라미터 page가 없다. 그래서 초기값 필요
		int page = 1; //보여줄 page
		int limit = 10; //한 페이지에 보여줄 게시판 목록 수
		
		if(request.getParameter("page") != null) page = Integer.parseInt(request.getParameter("page"));
		System.out.println("넘어온 페이지 = " + page);
		
		if(request.getParameter("limit") != null) limit = Integer.parseInt(request.getParameter("limit"));
		System.out.println("넘어온 limit = " + limit);
		
		
		//리스트를 받아옴
		List<ProjectComplete> cancelList = projectDAO.getCancelProjectsList(page, limit, userid);
		System.out.println(cancelList);

		
		//총 리스트 수 받아옴
		int listcount = projectDAO.getCancelCountList(userid);
		System.out.println("cancel 리스트 사이즈" + listcount);
		
		int maxpage = (listcount + limit -1) / limit;
		System.out.println("총 페이지 수 = " + maxpage);
		
		int startpage = ((page - 1) / 10) * 10 + 1;
		System.out.println("현재 페이지에 보여줄 시작 페이지 수 : " + startpage);
		
		//endpage : 현재 페이지 그룹에서 보여줄 마지막 페이지 수([10], [20], [30] 등..)
		int endpage = startpage + 10-1;


		if(endpage > maxpage) endpage = maxpage;
		
		System.out.println("현재 페이지에 보여줄 마지막 페이지 수 : " + endpage);
		String state = request.getParameter("state");
		if(state == null) {
			System.out.println("state == null");
			request.setAttribute("page", page); //현재 페이지 수 
			request.setAttribute("maxpage", maxpage); //최대 페이지 수 
			request.setAttribute("startpage", startpage); //현재 페이지에 표시할 첫 페이지 수
			request.setAttribute("endpage", endpage); //현재 페이지에 표시할 끝 페이지 수
			request.setAttribute("listcount", listcount); //총 글의 수 
			request.setAttribute("boardlist", cancelList); //해당 페이지의 글 목록을 갖고 있는 리스트
			request.setAttribute("limit", limit); 
			
			ActionForward forward = new ActionForward();
			forward.setPath("/WEB-INF/views/project/project_cancel.jsp"); 
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

			JsonElement je = new Gson().toJsonTree(cancelList);
			System.out.println("boardlist = " + je.toString());
			object.add("boardlist", je);
			

			
			
			
			response.setContentType("application/json; charset = utf-8");
			response.getWriter().print(object);
			System.out.println(object.toString());
			return null;
		}
    }
}
