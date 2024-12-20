package com.hta2405.unite.action;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hta2405.unite.dao.BoardDao;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dao.JobDao;
import com.hta2405.unite.dao.NoticeDao;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.dto.Job;
import com.hta2405.unite.util.LocalDateTimeAdapter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HomeAction implements Action {
	private final NoticeDao noticeDao = new NoticeDao();

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String userid = (String) req.getSession().getAttribute("id");
    	Long deptId = new EmpDao().getEmpById((String) req.getSession().getAttribute("id")).getDeptId();
    	
		req.setAttribute("noticeList", noticeDao.getAliveNotice());
		//유저 왼쪽 테이블
    	EmpDao empDao = new EmpDao();
    	JobDao jobDao = new JobDao();

		Emp userinfo = empDao.getEmpById(userid);
		Job job = jobDao.getJobByEmpId(userid);

		req.setAttribute("profile", userinfo);
		req.setAttribute("job", job.getJobName());

		
		// 게시판 가운데 테이블 데이터 생성
		if ("XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
	        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
	        BoardDao board = new BoardDao();
	        ArrayList<Object> list = board.getBoardListAll(deptId);
	        
	        
	        JsonObject object = new JsonObject();
	        JsonElement jeEmpMap = gson.toJsonTree(new EmpDao().getIdToENameMap());
	        JsonElement jeEmpUUIDMap = gson.toJsonTree(new EmpDao().getIdToENameUUIDMap());
	        
	        
	        JsonElement je1 = gson.toJsonTree(list.get(0));
	        JsonElement je2 = gson.toJsonTree(list.get(1));
	        
	        object.add("boards", je1);
	        object.add("posts", je2);
	        object.add("name", jeEmpMap);
	        object.add("emp", jeEmpUUIDMap);
	        System.out.println(jeEmpUUIDMap);
	        
	        resp.setContentType("application/json;charset=utf-8");
	        resp.getWriter().print(object);
	        return null;  // 여기서는 JSP로 포워드하지 않고 JSON만 반환
	    }

	    // 일반 요청일 경우 JSP 포워드
	    ActionForward forward = new ActionForward();
	    forward.setRedirect(false);
	    forward.setPath("/WEB-INF/views/home.jsp");
	    return forward;
    }
}
