package com.hta2405.unite.action.post;

import java.io.File;
import java.io.IOException;
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

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

public class PostAddAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		BoardDao boardDao = new BoardDao();
		
		Post postData = new Post();
		List<PostFile> postFiles = new ArrayList<>();
		
		HttpSession session = req.getSession();

		//boardName2로 boardId 구하기
        String boardName2 = req.getParameter("boardName2");
        
        if(boardName2 != null) {
        	Long boardId = (boardDao.getBoardListByName2(boardName2)).getBoardId();
    		
    		postData.setBoardId(boardId);
    		postData.setPostWriter((String) session.getAttribute("id"));
    		postData.setPostSubject(req.getParameter("board_subject"));
    		postData.setPostContent(req.getParameter("board_content"));
        }
		
		// 실제 저장 경로를 지정합니다.
		ServletContext sc = req.getServletContext();
		String realFolder = sc.getRealPath("boardupload");
		
		File uploadDirectory = new File(realFolder);
        // 디렉터리가 없으면 생성
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdir();
        }
        
        // 모든 파일 파트를 가져옴
        Collection<Part> fileParts = req.getParts();

        // 각 파일 처리
        for (Part filePart : fileParts) {
        	
            // 파일 파트인지 확인
            if (filePart.getContentType() != null && filePart.getSubmittedFileName().contains(".")) {
                // UUID로 고유한 파일명 생성
                String fileName = UUID.randomUUID().toString();
                
                // 원래 파일의 확장자 가져오기
                String originalFileName = filePart.getSubmittedFileName();
                
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                
                // 최종 저장 파일명 생성
                String savedFileName = fileName + fileExtension;
                
                // 파일 저장 경로 설정
                String filePath = realFolder + File.separator + savedFileName;
                filePart.write(filePath);//지정된 경로에 저장

        		PostFile postFileData = new PostFile();
    			postFileData.setPostFilePath(realFolder);
    			postFileData.setPostFileOriginal(originalFileName);
    			postFileData.setPostFileUUID(fileName);
    			postFileData.setPostFileType(fileExtension);
    			
    			//리스트에 저장
    			postFiles.add(postFileData);
            }
        }
        Boolean postAndFileCheck = boardDao.postAndFileInsert(postData, postFiles);
		JsonObject jObject = new JsonObject();
        
        if(postAndFileCheck) {
			jObject.addProperty("message", "게시판 등록 완료");
		}else {
			jObject.addProperty("message", "게시판 등록 실패");
		}
        resp.setContentType("application/json;charset=utf-8");
		resp.getWriter().print(jObject);
        return null;
        
	}

}
