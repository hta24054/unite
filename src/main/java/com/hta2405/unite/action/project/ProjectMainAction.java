	package com.hta2405.unite.action.project;
	
	import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ProjectDAO;
import com.hta2405.unite.dto.ProjectDetail;
import com.hta2405.unite.dto.ProjectInfo;
import com.hta2405.unite.util.ConfigUtil;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024 * 1, // 1MB
	    maxFileSize = 1024 * 1024 * 10,      // 10MB
	    maxRequestSize = 1024 * 1024 * 50    // 50MB
	)
	public class ProjectMainAction implements Action {
		private static final String UPLOAD_DIRECTORY = ConfigUtil.getProperty("project.upload.directory");
	    @Override
	    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	        String action = req.getParameter("action");

	        if ("updateStatus".equals(action)) {
	            int projectId = Integer.parseInt(req.getParameter("projectId"));
	            String status = req.getParameter("status");
	            ProjectDAO projectDAO = new ProjectDAO();
	            boolean success = false;

	            // 프로젝트 상태 업데이트
	            if ("completed".equals(status)) {
	                success = projectDAO.updateProjectStatus(projectId, true, false);
	            } else if ("cancelled".equals(status)) {
	                success = projectDAO.updateProjectStatus(projectId, false, true);
	            }

	            // 파일이 있을 경우에만 파일 업로드 처리
	            List<ProjectInfo> uploadedFiles = processFileUpload(req, resp);
	            if (!uploadedFiles.isEmpty()) {
	                // 파일 업로드 후 상태에 따른 처리
	                int fileStatus = "completed".equals(status) ? 1 : 2;
	                success &= projectDAO.saveUploadedFiles(projectId, uploadedFiles, fileStatus);
	            }

	            // 상태 업데이트 결과 반환
	            resp.setContentType("application/json");
	            resp.getWriter().write("{\"success\":" + success + "}");
	            return null;
	        }

	        ActionForward forward = new ActionForward();
	        forward.setPath("/WEB-INF/views/project/project_main.jsp");
	        forward.setRedirect(false);
	        return forward;
	    }

	    private List<ProjectInfo> processFileUpload(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
	        List<ProjectInfo> uploadedFiles = new ArrayList<>();
	        String uploadDir = UPLOAD_DIRECTORY;
	        
	        // 디렉토리가 없으면 생성
	        File uploadDirectory = new File(uploadDir);
	        if (!uploadDirectory.exists()) {
	            boolean dirCreated = uploadDirectory.mkdirs(); // 디렉토리 생성
	            if (!dirCreated) {
	                sendErrorResponse(resp, "디렉토리 생성에 실패했습니다.");
	                return uploadedFiles;
	            }
	        }

	        Collection<Part> fileParts = req.getParts();
	        for (Part filePart : fileParts) {
	            if (filePart.getContentType() != null) {
	                ProjectInfo fileDetail = new ProjectInfo();

	                String originalFileName = filePart.getSubmittedFileName();
	                String fileUUID = UUID.randomUUID().toString();
	                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
	                String savedFileName = fileUUID + fileExtension;
	                String filePath = uploadDir + File.separator + savedFileName;

	                try {
	                    filePart.write(filePath); // 파일 저장
	                } catch (IOException e) {
	                    sendErrorResponse(resp, "파일 저장에 실패했습니다.");
	                    e.printStackTrace();
	                    return uploadedFiles;
	                }

	                fileDetail.setProject_file_path(filePath);
	                fileDetail.setProject_file_original(originalFileName);
	                fileDetail.setProject_file_uuid(fileUUID);
	                fileDetail.setProject_file_type(fileExtension);

	                uploadedFiles.add(fileDetail); // 파일 정보 리스트에 추가
	            }
	        }

	        return uploadedFiles;
	    }

	    private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
	        response.setContentType("application/json; charset=utf-8");
	        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        response.getWriter().write(new Gson().toJson(Map.of("error", errorMessage)));
	    }
	}

