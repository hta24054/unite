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
		System.out.println("전list="+list);
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
		System.out.println("realFolder = "+ realFolder);
		
		// 모든 파일 파트를 가져옴
        Collection<Part> fileParts = req.getParts();

        // 각 파일 처리
        for (Part filePart : fileParts) {
        	
        	System.out.println("filePart ContentType=" + filePart.getContentType());
        	System.out.println("filePart Headers=" + filePart.getHeaderNames());
        	
            // 파일 파트인지 확인
            if (filePart.getContentType() != null && filePart.getSubmittedFileName().contains(".")) {
                // UUID로 고유한 파일명 생성
                String fileName = UUID.randomUUID().toString();
                System.out.println("fileName="+fileName);
                
                // 원래 파일의 확장자 가져오기
                String originalFileName = filePart.getSubmittedFileName();
                System.out.println("originalFileName="+originalFileName);
                
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                System.out.println("fileExtension="+fileExtension);
                
                // 최종 저장 파일명 생성
                String savedFileName = fileName + fileExtension;
                
                // 파일 저장 경로 설정
                String filePath = realFolder + File.separator + savedFileName;
                filePart.write(filePath);//지정된 경로에 저장
                System.out.println("filePath="+filePath);
                
                // 저장된 파일명 출력 (필요에 따라 다른 처리 가능)
                System.out.println("Saved file: " + savedFileName);
                

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
            	System.out.println("Value: " + fieldValue);
            	
            	// UUID 추출을 위한 정규식
                Pattern pattern = Pattern.compile("\"postFileUUID\":\"([a-f0-9\\-]+)\"");
                Matcher matcher = pattern.matcher(fieldValue);

                if (matcher.find()) {
                    String postFileUUID = matcher.group(1);
                    System.out.println("deleted UUID: " + postFileUUID);

                    // 삭제 리스트에 추가
                    deletePostFileUUIDList.add(postFileUUID);
                } else {
                    System.out.println("UUID not found in fieldValue");
                }
        	} else { // 일반 텍스트 필드인 경우
                String fieldName = filePart.getName();
                String fieldValue = new String(filePart.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                System.out.println("Field Part: " + fieldName);
                System.out.println("Value: " + fieldValue);
            }
        }
		System.out.println("postData"+postData);
		System.out.println("postFileList="+postFileList);
		
        Boolean postModifyCheck = boardDao.postAndFileModify(postData, postFileList, deletePostFileUUIDList);
        
		JsonObject jObject = new JsonObject();
        
        if(postModifyCheck) {
			System.out.println("게시판 수정 완료");
			jObject.addProperty("message", "게시판 수정 완료");
		}else {
			System.out.println("게시판 수정 실패");
			jObject.addProperty("message", "게시판 수정 실패");
		}
		jObject.addProperty("postId", postData.getPostId());
		jObject.addProperty("boardName2", boardName2);
		
        resp.setContentType("application/json;charset=utf-8");
		resp.getWriter().print(jObject);
		System.out.println(jObject.toString());
        return null;
	}

}
