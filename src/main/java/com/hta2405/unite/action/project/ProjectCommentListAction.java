package com.hta2405.unite.action.project;

import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ProjectbDao;
import com.hta2405.unite.util.ProjectUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//프로젝트 상세. main에서 누르고 들어오는 첫 창
public class ProjectCommentListAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String userid = (String) req.getSession().getAttribute("id");
        int projectid = (Integer) req.getSession().getAttribute("projectId");
        int task_num = Integer.parseInt(req.getParameter("comment_board_num"));
        System.out.println(userid);
        System.out.println(projectid);
        System.out.println(task_num);
        
        
        
        ProjectbDao dao = new ProjectbDao();
        
        int comment_board_num = Integer.parseInt(req.getParameter("comment_board_num"));
        int state = Integer.parseInt(req.getParameter("state"));
		int listcount = dao.getListCount(comment_board_num);
		System.out.println("comment_board_num : " + comment_board_num);
		System.out.println("state : " + state);
		
		JsonObject object = new JsonObject();
		object.addProperty("listcount", listcount);
		
		JsonArray jarray = dao.getCommentList(comment_board_num, state);
		JsonElement je = new Gson().toJsonTree(jarray);
		object.add("commentlist", je);
		
		object.addProperty("id", userid); 
		
		resp.setContentType("application/json; charset=utf-8");
		PrintWriter out = resp.getWriter();
		out.print(object.toString());
		System.out.println(object.toString());
		
		return null;
    }


}
