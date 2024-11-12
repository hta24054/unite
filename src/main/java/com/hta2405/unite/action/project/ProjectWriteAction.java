package com.hta2405.unite.action.project;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ProjectbDao;
import com.hta2405.unite.dto.ProjectTask;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ProjectWriteAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 세션에서 사용자 ID와 프로젝트 ID 가져오기
        String userid = (String) request.getSession().getAttribute("id");
        int projectid = (Integer) request.getSession().getAttribute("projectId");

        // 폼에서 제목과 내용 가져오기
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        System.out.println("Title: " + title);
        System.out.println("Content: " + content);
     
        // 파일 업로드 처리 주석 처리 (파일 업로드 안 할 경우)
        // String filePath = null;
        // Part filePart = request.getPart("file");
        // if (filePart != null && filePart.getSize() > 0) {
        //     String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        //     filePath = "/uploads/" + fileName;
        //     String uploadDir = request.getServletContext().getRealPath("/uploads");
        //     filePart.write(uploadDir + "/" + fileName);
        // }


        // ProjectbDao 객체 생성 후 글 작성 처리
        ProjectbDao projectbDao = new ProjectbDao();
        boolean isInserted = projectbDao.insertOrUpdatePost(title, content, null, userid, projectid);  // 파일 경로는 null로 설정

        // 글 작성 실패 시 JSON 에러 메시지 반환
        if (!isInserted) {
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "글 작성에 실패했습니다.");
            response.getWriter().write(new Gson().toJson(errorResponse));
            return null;
        }

        // 최신 3개의 글 가져오기
        List<ProjectTask> recentPosts = projectbDao.getRecentPosts(userid, projectid);

        // JSON 형식의 문자열로 응답 생성
        StringBuilder jsonResponse = new StringBuilder();
        jsonResponse.append("{");
        jsonResponse.append("\"success\": true,");
        jsonResponse.append("\"posts\": ").append(new Gson().toJson(recentPosts));
        jsonResponse.append("}");

        // 응답 헤더 설정 및 JSON 반환
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("UTF-8");

        // 응답으로 JSON 문자열을 실제로 보내기
        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();  // 출력 스트림을 플러시하여 응답이 전송되도록 함

        return null;
    }




}