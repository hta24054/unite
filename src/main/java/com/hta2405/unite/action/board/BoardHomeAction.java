package com.hta2405.unite.action.board;

import java.io.IOException;
import java.util.List;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.BoardDao;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Board;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BoardHomeAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Long DeptId = new EmpDao().getEmpById((String) req.getSession().getAttribute("id")).getDeptId();
		List<Board> BoardList = new BoardDao().getBoardListByDeptId(DeptId);
		
		System.out.println("boardScope= "+BoardList);
		req.getSession().setAttribute("boardScope", BoardList);
		
		ActionForward forward = new ActionForward();
		forward.setRedirect(false);
		forward.setPath("/WEB-INF/views/board/boardHome.jsp");
		return forward;
	}
}