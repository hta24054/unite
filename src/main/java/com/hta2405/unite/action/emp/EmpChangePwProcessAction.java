package com.hta2405.unite.action.emp;

import java.io.IOException;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.util.CommonUtil;
import com.hta2405.unite.util.EmpUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class EmpChangePwProcessAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		HttpSession session = req.getSession();
        String newPassword = req.getParameter("newPassword");
        String id = (String) session.getAttribute("checkId");
        
        EmpDao empDao = new EmpDao();
        Emp emp = empDao.getEmpById(id);
        
        session.invalidate();//모든 세션 삭제
        
        if(emp.getPassword().equals(EmpUtil.hashingPassword(newPassword))) {//이전 비밀번호와 바꿀 비밀번호가 동일할 경우
        	return CommonUtil.alertAndGoBack(resp, "이전 비밀번호와 동일한 비밀번호로 변경하실 수 없습니다.");
        	
        } else if(!EmpUtil.changePassword(emp, newPassword)) {//비밀번호 변경 실패
            return CommonUtil.alertAndGoBack(resp, "비밀번호 변경 실패");
            
        } else {//비밀번호 변경 성공
        	req.getSession().setAttribute("message","비밀번호 변경이 완료되었습니다.");
            return new ActionForward(true, "login");
        }
	}

}
