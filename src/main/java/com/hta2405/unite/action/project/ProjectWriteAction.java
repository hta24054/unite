package com.hta2405.unite.action.project;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ProjectbDao;
import com.hta2405.unite.dto.ProjectTask;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

public class ProjectWriteAction implements Action {

    private static final String UPLOAD_DIRECTORY = "upload"; // 파일 업로드 디렉토리

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 세션에서 사용자 ID와 프로젝트 ID 가져오기
        String userid = (String) request.getSession().getAttribute("id");
        int projectid = (Integer) request.getSession().getAttribute("projectId");

        // 폼에서 제목과 내용 가져오기
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        // 파일 업로드 처리
        String filePath = null;
        String originalFileName = null;
        String fileUuid = null;
        String fileType = null;

        // 파일 첨부가 있을 경우
        Part filePart = request.getPart("file");  // "file"은 HTML form에서 파일 input의 name
        if (filePart != null && filePart.getSize() > 0) {
            // 파일의 원본 이름
            originalFileName = Path.of(filePart.getSubmittedFileName()).getFileName().toString();

            // UUID를 사용하여 고유 파일 이름 생성
            fileUuid = UUID.randomUUID().toString();

            // 파일 MIME 타입
            fileType = filePart.getContentType();

            // 업로드 디렉토리 경로
            String uploadDir = request.getServletContext().getRealPath("/") + UPLOAD_DIRECTORY;
            File uploadDirectory = new File(uploadDir);
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdir();  // 디렉토리가 없으면 생성
            }

            // 파일 저장 경로
            filePath = uploadDir + File.separator + fileUuid + "_" + originalFileName;
            File file = new File(filePath);

            // 파일을 저장
            try {
                filePart.write(file.getAbsolutePath());
            } catch (IOException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json; charset=utf-8");
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "파일 업로드 중 오류가 발생했습니다.");
                response.getWriter().write(new Gson().toJson(errorResponse));
                return null;
            }
        }

        // ProjectbDao 객체 생성 후 글 작성 처리
        ProjectbDao projectbDao = new ProjectbDao();
        boolean isInserted = projectbDao.insertOrUpdatePost(title, content, filePath, originalFileName, fileUuid, fileType, userid, projectid);

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
