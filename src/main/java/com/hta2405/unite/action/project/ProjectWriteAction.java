package com.hta2405.unite.action.project;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ProjectbDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ProjectWriteAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userid = (String) request.getSession().getAttribute("id");
        int projectid = (Integer) request.getSession().getAttribute("projectId");
        System.out.println(projectid);
        // 요청에서 파라미터를 가져옴
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String filePath = null;  // 파일 경로가 필요하다면 설정

        /*        // 파일 업로드 및 파일 경로 설정 예시
        Part filePart = request.getPart("file");
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            filePath = "/uploads/" + fileName;
            String uploadDir = request.getServletContext().getRealPath("/uploads");
            filePart.write(uploadDir + "/" + fileName);  // 파일을 서버에 저장
        }*/

        // ProjectbDao 객체 생성 후 insertOrUpdatePost 메서드 호출
        ProjectbDao projectbDao = new ProjectbDao();
        boolean isInserted = projectbDao.insertOrUpdatePost(title, content, filePath, userid, projectid);

        if (!isInserted) {
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "글 작성에 실패했습니다.");
            response.getWriter().write(new Gson().toJson(errorResponse));
            return null;
        }

        /* 삽입한 게시글을 다시 조회
        List<ProjectTask> newPostList = projectbDao.getRecentPosts();  // 예: 최근 작성된 게시글 조회 메서드

        // JSON으로 변환하여 클라이언트에 응답
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(newPostList);
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(jsonResponse);*/

        return null;
    }
}
