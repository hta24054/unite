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
public class ProjectSaveSessionAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 클라이언트로부터 전달된 memberId를 파라미터로 받기
        String memberId = request.getParameter("memberId");

        // memberId가 null이 아니면 세션에 저장
        if (memberId != null && !memberId.isEmpty()) {
            // 세션에 memberId 저장
            request.getSession().setAttribute("memberId", memberId);
        }

        // 성공적인 응답을 반환하기 위해, 클라이언트로 응답을 보내지 않도록 설정
        // 여기서는 필요하다면 특정 페이지로 리다이렉트하거나, 상태코드를 보내도록 할 수 있습니다.

        // 예: 리다이렉트
        response.sendRedirect("projectb/list");  // memberId를 저장하고 목록 페이지로 리다이렉트

        // ActionForward 객체 반환 (리다이렉트하기 때문에 반환할 필요 없음)
        return null;
    }
}

