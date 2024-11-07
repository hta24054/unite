package com.hta2405.unite.action;

import java.io.IOException;

import com.hta2405.unite.util.CommonUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EmpChangePwAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		

		Boolean step1Complete = (Boolean) req.getSession().getAttribute("step1Complete");
	    Boolean step2Complete = (Boolean) req.getSession().getAttribute("step2Complete");

	    if (step1Complete == null || !step1Complete || step2Complete == null || !step2Complete) {
	        // 이전 단계가 완료되지 않았으면 이메일 인증 폼으로 리다이렉트
            return CommonUtil.alertAndGoBack(resp, "정상적이지 않은 접근 경로입니다.");
	    }
		
		return new ActionForward(false, "/WEB-INF/views/emp/changePw.jsp");
	}

}
