package com.hta2405.unite.action;

import java.io.IOException;

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
    	
    	EmpDao empDao = new EmpDao();
    	JobDao jobDao = new JobDao();
    	
		Emp userinfo = empDao.getEmpById(userid);
		req.setAttribute("profile", userinfo);
		
		Job job = jobDao.getJobByEmpId(userid);
		req.setAttribute("job", job.getJobName());
		
        return new ActionForward(false, "/WEB-INF/views/home.jsp");
    }
}
