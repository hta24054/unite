package com.hta2405.unite.action.postcomments;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.PostCommentDao;
import com.hta2405.unite.dto.PostComment;
import com.hta2405.unite.util.ConfigUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

public class PostCommentsUpdateAction implements Action {
	private static final String UPLOAD_DIRECTORY = ConfigUtil.getProperty("postComment.upload.directory");
    private final PostCommentDao postCommentDao = new PostCommentDao();
    
	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PostComment postCommentData = new PostComment();
		postCommentData.setPostCommentContent(req.getParameter("postCommentContent"));
		
		// 파일 처리 (FormData로 보낸 파일)
        Part filePart = req.getPart("file"); // FormData에서 보낸 'file' 파트 처리
        String filePath = null;
        String fileOriginalName = null;
        String fileUUID = null;
        String fileType = null;
		
		if (filePart != null && filePart.getSize() > 0) {
            // 파일 이름과 타입
            fileOriginalName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            fileType = filePart.getContentType();

            // UUID 생성하여 파일명에 적용
            fileUUID = UUID.randomUUID().toString();
            String fileName = fileUUID + "_" + fileOriginalName;

            // 저장 경로 설정 및 파일 저장
            String uploadPath = UPLOAD_DIRECTORY;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists() && !uploadDir.mkdirs()) {
                throw new IOException("업로드 폴더를 생성할 수 없습니다: " + uploadPath);
            }

            // 파일 저장
            File file = new File(uploadPath, fileName);
            filePart.write(file.getAbsolutePath()); // 파일을 디스크에 저장
            filePath = UPLOAD_DIRECTORY + "/" + fileName;
        }
        
        // 파일 경로와 메타 정보 설정
        postCommentData.setPostCommentFilePath(filePath);
        postCommentData.setPostCommentFileOriginal(fileOriginalName);
        postCommentData.setPostCommentFileUUID(fileUUID);
        postCommentData.setPostCommentFileType(fileType);
		
		
		postCommentData.setCommentId(Long.parseLong(req.getParameter("commentId")));
		
		int ok = postCommentDao.commentsUpdate(postCommentData);
		resp.getWriter().print(ok);
		return null;
	}
}
