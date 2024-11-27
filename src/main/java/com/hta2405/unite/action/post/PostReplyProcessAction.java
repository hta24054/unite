package com.hta2405.unite.action.post;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.google.gson.JsonObject;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.BoardDao;
import com.hta2405.unite.dto.Post;
import com.hta2405.unite.dto.PostFile;
import com.hta2405.unite.util.ConfigUtil;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

public class PostReplyProcessAction implements Action {
	private static final String UPLOAD_DIRECTORY = ConfigUtil.getProperty("post.upload.directory");
	
	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		BoardDao boardDao = new BoardDao();
		
		Post postData = new Post();
		List<PostFile> postFiles = new ArrayList<>();
		
		HttpSession session = req.getSession();
		
        // 모든 파일 파트를 가져옴
        Collection<Part> fileParts = req.getParts();

        // 각 파일 처리
        for (Part filePart : fileParts) {
        	String filePath = null;
            String fileOriginalName = null;
            String fileUUID = null;
            String fileType = null;
            
            // 파일 파트인지 확인
            if (filePart.getContentType() != null && filePart.getSubmittedFileName().contains(".")) {
            	// 파일 이름과 타입
                fileOriginalName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                fileType = filePart.getContentType();
                System.out.println(fileType);

                // UUID 생성하여 파일명에 적용
                fileUUID = UUID.randomUUID().toString();
                String fileName = fileUUID + "_" + fileOriginalName;

                // 저장 경로 설정 및 파일 저장
                String uploadPath = UPLOAD_DIRECTORY;
                System.out.println(uploadPath);
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists() && !uploadDir.mkdirs()) {
                    throw new IOException("업로드 폴더를 생성할 수 없습니다: " + uploadPath);
                }

                // 파일 저장
                File file = new File(uploadPath, fileName);
                filePart.write(file.getAbsolutePath());// 웹 경로 설정
                filePath = UPLOAD_DIRECTORY;
            	
                
        		PostFile postFileData = new PostFile();
    			postFileData.setPostFilePath(filePath);
    			postFileData.setPostFileOriginal(fileOriginalName);
    			postFileData.setPostFileUUID(fileUUID);
    			postFileData.setPostFileType(fileType);
    			
    			//리스트에 저장
    			postFiles.add(postFileData);
            } else if(filePart.getName().equals("boardName2Hidden")){
            	String boardName2 = new String(filePart.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            	if(boardName2 != null) {
                	Long boardId = (boardDao.getBoardListByName2(boardName2)).getBoardId();
            		
            		postData.setBoardId(boardId);
            		postData.setPostWriter((String) session.getAttribute("id"));
            		postData.setPostSubject(req.getParameter("board_subject"));
            		postData.setPostContent(req.getParameter("board_content"));
            		postData.setPostReRef(Long.parseLong(req.getParameter("board_re_ref")));
            		postData.setPostReLev(Long.parseLong(req.getParameter("board_re_lev")));
            		postData.setPostReSeq(Long.parseLong(req.getParameter("board_re_seq")));
                }
            }
        }
		
        Long postId = boardDao.postAndFileReply(postData, postFiles);
		JsonObject jObject = new JsonObject();
        
        if(postId>0) {
			jObject.addProperty("message", "게시판 답글 완료");
			jObject.addProperty("url", postId);
		}else {
			jObject.addProperty("message", "게시판 답글 실패");
			jObject.addProperty("url", Long.parseLong(req.getParameter("postId")));
		}
        resp.setContentType("application/json;charset=utf-8");
		resp.getWriter().print(jObject);
        return null;
	}

}
