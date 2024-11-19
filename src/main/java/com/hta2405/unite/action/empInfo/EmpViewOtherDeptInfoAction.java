package com.hta2405.unite.action.empInfo;

import java.io.IOException;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 타 부서 인사정보 페이지로 포워드하는 액션 클래스입니다.
 */
public class EmpViewOtherDeptInfoAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 타 부서 인사정보 페이지로 포워드 설정을 생성합니다.
        ActionForward forward = new ActionForward();
        forward.setPath("/WEB-INF/views/empInfo/otherDeptInfo.jsp");
        forward.setRedirect(false); // 포워드 방식으로 설정
        return forward;
    }
}
