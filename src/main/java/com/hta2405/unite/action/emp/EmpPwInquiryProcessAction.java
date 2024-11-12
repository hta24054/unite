package com.hta2405.unite.action.emp;

import java.io.IOException;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.util.CommonUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class EmpPwInquiryProcessAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String id = req.getParameter("id");
		
		EmpDao dao = new EmpDao();
		Emp emp = dao.getEmpById(id);
		
		if(emp != null) {
			String dbEmail = emp.getEmail();
			
			HttpSession session = req.getSession();
			session.setAttribute("checkId", id);
			session.setAttribute("email", maskEmail(dbEmail));
			session.setMaxInactiveInterval(5*60);//세션 유효시간 5분 설정
			
			session.setAttribute("step1Complete", true);//세션 플래그(비밀번호 변경 페이지)
			
			return new ActionForward(true, "emailVerification");//emailVerification로 이동
		}
		
		return CommonUtil.alertAndGoBack(resp, "잘못된 아이디입니다. 다시 입력해주세요");//요청한 곳으로 돌아감
	}
	
	public static String maskEmail(String email) {
        // 이메일이 null이거나 비어있을 경우 그대로 반환
        if (email == null || email.isEmpty()) {
            return email;
        }

        // 이메일을 '@' 기준으로 나눕니다.
        String[] parts = email.split("@");

        String username = parts[0];
        String domain = parts[1];

        int length = username.length();
        if (length <= 3) {
            // 사용자명이 3자리이하면 모두 감추기
            username = "*".repeat(length);
        } else {
            // 사용자명이 3자리이상이면 첫 두글자 제외하고 모두 감추기
            username = username.substring(0, 2)
                    + "*".repeat(length - 2);
        }

        // 마스킹된 사용자명과 도메인을 합쳐서 반환
        return username + "@" + domain;
    }

}
