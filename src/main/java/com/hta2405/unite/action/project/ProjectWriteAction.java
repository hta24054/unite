package com.hta2405.unite.action.project;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ProjectbDao;
import com.hta2405.unite.dto.ProjectDetail;
import com.hta2405.unite.dto.ProjectInfo;
import com.hta2405.unite.dto.ProjectTask;
import com.hta2405.unite.util.ConfigUtil;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024 * 1,  // 1MB
	    maxFileSize = 1024 * 1024 * 10,       // 10MB
	    maxRequestSize = 1024 * 1024 * 50     // 50MB
	)
public class ProjectWriteAction implements Action {
    private static final String UPLOAD_DIRECTORY = ConfigUtil.getProperty("projecttask.upload.directory");

    public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 세션에서 사용자 ID와 프로젝트 ID 가져오기
        String userid = (String) request.getSession().getAttribute("id");
        int projectid = (Integer) request.getSession().getAttribute("projectId");

        // 폼에서 제목과 내용 가져오기
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        if (title == null || title.trim().isEmpty() || content == null || content.trim().isEmpty()) {
            sendErrorResponse(response, "제목과 내용은 필수 입력 사항입니다.");
            return null;
        }

        // 파일 업로드 처리
        List<ProjectDetail> uploadedFiles = processFileUpload(request, response);

        // DAO 호출하여 DB에 글과 파일 정보 저장
        ProjectbDao projectbDao = new ProjectbDao();
        boolean isInserted = projectbDao.insertOrUpdatePost(title, content, uploadedFiles, userid, projectid);
        System.out.println("DB 삽입 결과: " + isInserted);

        if (!isInserted) {
            sendErrorResponse(response, "글 작성에 실패했습니다.");
            return null;
        }

        // 글 작성 후 최신 게시글 목록 가져오기
        List<ProjectTask> recentPosts = projectbDao.getRecentPosts(userid, projectid);
        String jsonResponse = new Gson().toJson(Map.of("success", true, "posts", recentPosts));
        System.out.println("글 작성 후 최신 게시글: " + recentPosts);

        // 성공적인 응답 반환
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();

        return null;
    }

    private List<ProjectDetail> processFileUpload(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        List<ProjectDetail> taskFile = new ArrayList<>();
        String uploadDir = UPLOAD_DIRECTORY;

        // 디렉토리가 없으면 생성
        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            boolean dirCreated = uploadDirectory.mkdirs();
            if (!dirCreated) {
                sendErrorResponse(resp, "디렉토리 생성에 실패했습니다.");
                return taskFile;
            }
        }

        // 파일 업로드 처리
        Collection<Part> fileParts = req.getParts();
        for (Part filePart : fileParts) {
            if (filePart.getContentType() != null) {
                // 파일 크기 체크 (0일 경우 업로드하지 않도록 처리)
                if (filePart.getSize() == 0) {
                    continue; // 파일 크기가 0인 경우 건너뛰기
                }

                ProjectDetail task = new ProjectDetail();
                String fileName = UUID.randomUUID().toString(); // 고유한 파일 이름 생성
                String originalFileName = filePart.getSubmittedFileName();
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                String savedFileName = fileName + fileExtension;
                String filePath = uploadDir + File.separator + savedFileName;

                try {
                    filePart.write(filePath); // 파일 저장
                    System.out.println("파일 저장 성공: " + filePath);
                } catch (IOException e) {
                    sendErrorResponse(resp, "파일 저장에 실패했습니다.");
                    e.printStackTrace();
                    return null;
                }

                task.setTask_file_path(filePath);
                task.setTask_file_original(originalFileName);
                task.setTask_file_uuid(fileName);
                task.setTask_file_type(fileExtension);

                taskFile.add(task);
            }
        }

        return taskFile;
    }

    private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", errorMessage);
        response.getWriter().write(new Gson().toJson(errorResponse));
    }
}

