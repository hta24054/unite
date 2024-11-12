package com.hta2405.unite.action.post;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.BoardDao;
import com.hta2405.unite.dao.DeptDao;
import com.hta2405.unite.dto.Post;
import com.hta2405.unite.dto.PostFile;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

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
		DeptDao deptDao = new DeptDao();
		
		Post postData = new Post();
		PostFile postFileData = new PostFile();
		List<PostFile> postFiles = new ArrayList<>();

		//boardName2로 boardId 구하기
        String boardName2 = req.getParameter("boardName2");
		Long boardId = (boardDao.getBoardListByName2(boardName2)).getBoardId();
		
		ActionForward forward = new ActionForward();
		HttpSession session = req.getSession();
		
		//int fileSize = 5 * 1024 * 1024; // 업로드할 파일의 최대 사이즈 입니다. 5MB
		
		// 실제 저장 경로를 지정합니다.
		ServletContext sc = req.getServletContext();
		String realFolder = sc.getRealPath("boardupload");
		System.out.println("realFolder = "+ realFolder);
		
		File uploadDirectory = new File(realFolder);
        // 디렉터리가 없으면 생성
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdir();
        }
		
		postData.setBoardId(boardId);
		postData.setPostWriter((String) session.getAttribute("ename"));
		postData.setPostSubject(req.getParameter("board_subject"));
		postData.setPostContent(req.getParameter("board_content"));
        
        // 모든 파일 파트를 가져옴
        Collection<Part> fileParts = req.getParts();

        // 각 파일 처리
        for (Part filePart : fileParts) {
            // 파일 파트인지 확인
            if (filePart.getContentType() != null) {
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
			System.out.println("게시판 등록 완료");
			jObject.addProperty("message", "게시판 등록 완료");
		}else {
			System.out.println("게시판 등록 실패");
			jObject.addProperty("message", "게시판 등록 실패");
		}
        resp.setContentType("application/json;charset=utf-8");
		resp.getWriter().print(jObject);
		System.out.println(jObject.toString());
        return null;
        
	}

}
