package com.hta2405.unite.action.project;

import java.io.File;
import java.io.IOException;
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
import com.hta2405.unite.dto.ProjectTask;
import com.hta2405.unite.util.ConfigUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

//프로젝트 상세. main에서 누르고 들어오는 첫 창
public class ProjectModifyProcessAction implements Action {
	private static final String UPLOAD_DIRECTORY = ConfigUtil.getProperty("project.upload.directory");
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userid = req.getParameter("userid");
        int projectid = (Integer) req.getSession().getAttribute("projectId");
        int task_num = Integer.parseInt(req.getParameter("num"));
        System.out.println(userid);
        System.out.println(projectid);
        System.out.println(task_num);

        ActionForward forward = new ActionForward();
        ProjectbDao task_modify = new ProjectbDao();
        ProjectTask modify = new ProjectTask();

        // 기존 게시글 정보 업데이트
        modify.setProjectTitle(req.getParameter("board_subject"));
        modify.setProjectContent(req.getParameter("board_content"));
        modify.setTaskNum(task_num);
        modify.setProjectId(projectid);
        modify.setMemberId(userid);

        // 파일 업로드 처리
        List<ProjectDetail> uploadedFiles = processFileUpload(req, resp);
        if (!uploadedFiles.isEmpty()) {
            // 첨부파일 정보 설정 (여기서는 첫 번째 파일만 처리)
            ProjectDetail fileDetail = uploadedFiles.get(0);
            modify.setTask_file_original(fileDetail.getTask_file_original());
            modify.setTask_file_uuid(fileDetail.getTask_file_uuid());
            modify.setTask_file_type(fileDetail.getTask_file_type());

            // 기존 파일 삭제 로직 추가 (필요 시)
            String existingFilePath = task_modify.getExistingFilePath(task_num, projectid);
            if (existingFilePath != null) {
                File existingFile = new File(existingFilePath);
                if (existingFile.exists()) {
                    boolean deleted = existingFile.delete();
                    System.out.println("기존 파일 삭제: " + deleted);
                }
            }
        }

        // 수정 작업 처리
        boolean result = task_modify.modify(modify);
        System.out.println(result);
        if (!result) {
            System.out.println("게시판 수정 실패");
            forward.setRedirect(false);
            req.setAttribute("message", "게시판 수정 오류입니다");
            forward.setPath("/WEB-INF/views/error/error.jsp");
        } else {
            System.out.println("게시판 수정 완료");

            // 리다이렉트할 때 쿼리 스트링 없이 이동
            forward.setRedirect(true);
            forward.setPath("comm");
        }

        return forward;
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
