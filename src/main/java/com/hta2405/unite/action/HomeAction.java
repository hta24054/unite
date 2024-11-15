package com.hta2405.unite.action;

import java.io.IOException;
import java.util.ArrayList;

import com.hta2405.unite.dao.BoardDao;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dao.JobDao;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.dto.Job;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HomeAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String userid = (String) req.getSession().getAttribute("id");
    	
    	//유저 왼쪽 테이블
    	EmpDao empDao = new EmpDao();
    	JobDao jobDao = new JobDao();
    	
		Emp userinfo = empDao.getEmpById(userid);
		Job job = jobDao.getJobByEmpId(userid);

		req.setAttribute("profile", userinfo);
		req.setAttribute("job", job.getJobName());
		
		
		//게시판 가운데 테이블
		BoardDao board = new BoardDao();
		ArrayList<Object> list = board.getBoardListAll();
		req.setAttribute("board", list);
		
        return new ActionForward(false, "/WEB-INF/views/home.jsp");
    }
}
