package com.hta2405.unite.action.post;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

public class PostReplyProcessAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		BoardDao boardDao = new BoardDao();
		
		Post postData = new Post();
		List<PostFile> postFiles = new ArrayList<>();
		
		HttpSession session = req.getSession();
		
		// 실제 저장 경로를 지정합니다.
		ServletContext sc = req.getServletContext();
		String realFolder = sc.getRealPath("boardupload");
		System.out.println("realFolder = "+ realFolder);
		
		File uploadDirectory = new File(realFolder);
        // 디렉터리가 없으면 생성
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdir();
        }
        
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
                
                
                if (originalFileName == null) {
                    System.out.println("Error: originalFileName is null");
                } else {
                    System.out.println("originalFileName=" + originalFileName);
                }
                
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
    			postFileData.setPostFilePath(realFolder);
    			postFileData.setPostFileOriginal(originalFileName);
    			postFileData.setPostFileUUID(fileName);
    			postFileData.setPostFileType(fileExtension);
    			
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
            		
            		System.out.println("postData="+postData);
                }
            }else { // 일반 텍스트 필드인 경우
                String fieldName = filePart.getName();
                String fieldValue = new String(filePart.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                System.out.println("Field Part: " + fieldName);
                System.out.println("Value: " + fieldValue);
            }
        }
		System.out.println("postFiles="+postFiles);
		
        Long postId = boardDao.postAndFileReply(postData, postFiles);
		JsonObject jObject = new JsonObject();
        
        if(postId>0) {
			System.out.println("게시판 등록 완료");
			jObject.addProperty("message", "게시판 답글 완료");
			jObject.addProperty("url", postId);
		}else {
			System.out.println("게시판 등록 실패");
			jObject.addProperty("message", "게시판 답글 실패");
			jObject.addProperty("url", Long.parseLong(req.getParameter("postId")));
		}
        resp.setContentType("application/json;charset=utf-8");
		resp.getWriter().print(jObject);
		System.out.println(jObject.toString());
        return null;
	}

}