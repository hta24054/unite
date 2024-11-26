package com.hta2405.unite.action.post;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import jakarta.servlet.http.Part;

public class PostModifyProcessAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		BoardDao boardDao = new BoardDao();
		
		long postId = Long.parseLong(req.getParameter("postId"));
		List<Object> list = boardDao.getDetail(postId); //post emp postFileList 존재

		Post postData = (Post) list.get(0);
		List<String> deletePostFileUUIDList = new ArrayList<>();
		List<PostFile> postFileList = new ArrayList<>();
		
		String boardName2 = req.getParameter("boardName2");
		long boardId = (boardDao.getBoardListByName2(boardName2)).getBoardId();
		
		postData.setBoardId(boardId);
		postData.setPostSubject(req.getParameter("board_subject"));
		postData.setPostContent(req.getParameter("board_content"));
		
		// 실제 저장 경로를 지정합니다.
		ServletContext sc = req.getServletContext();
		String realFolder = sc.getRealPath("boardupload");
		
		
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
        		postFileData.setPostId(postId);
        		postFileData.setPostFilePath(realFolder);
    			postFileData.setPostFileOriginal(originalFileName);
    			postFileData.setPostFileUUID(fileName);
    			postFileData.setPostFileType(fileExtension);
    			
    			//리스트에 추가할 파일 저장
    			postFileList.add(postFileData);
    			
            } else if(filePart.getName().equals("deleted_files")) {
            	String fieldValue = new String(filePart.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            	
            	// UUID 추출을 위한 정규식
                Pattern pattern = Pattern.compile("\"postFileUUID\":\"([a-f0-9\\-]+)\"");
                Matcher matcher = pattern.matcher(fieldValue);

                if (matcher.find()) {
                    String postFileUUID = matcher.group(1);

                    // 삭제 리스트에 추가
                    deletePostFileUUIDList.add(postFileUUID);
                }
        	}
        }
		
        Boolean postModifyCheck = boardDao.postAndFileModify(postData, postFileList, deletePostFileUUIDList);
        
		JsonObject jObject = new JsonObject();
        
        if(postModifyCheck) {
			jObject.addProperty("message", "게시판 수정 완료");
		}else {
			jObject.addProperty("message", "게시판 수정 실패");
		}
		jObject.addProperty("postId", postData.getPostId());
		jObject.addProperty("boardName2", boardName2);
		
        resp.setContentType("application/json;charset=utf-8");
		resp.getWriter().print(jObject);
        return null;
	}

}
