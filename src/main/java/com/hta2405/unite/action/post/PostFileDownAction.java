package com.hta2405.unite.action.post;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.BoardDao;
import com.hta2405.unite.dto.PostFile;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PostFileDownAction implements Action {
	
	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		BoardDao boardDao = new BoardDao();
		
		Long postFileId = Long.parseLong(req.getParameter("postFileId"));
		
		PostFile postFileData = boardDao.getPostFile(postFileId);
		
		String savePath = "boardupload";
		
		//서블릿의 실행 환경 정보를 담고 있는 객체를 리턴합니다.
		ServletContext context = req.getServletContext();
		String sDownloadPath = context.getRealPath(savePath);
		
		String sFilePath = sDownloadPath + File.separator + postFileData.getPostFileUUID() + postFileData.getPostFileType();
		
		byte b[] = new byte[4096];
		
		// sFilePath에 있는 파일의 MimeType을 구해옵니다.
		String sMimeType = context.getMimeType(sFilePath);
		
		if(sMimeType == null) {
			sMimeType = "application/octet-stream";
		}
		
		resp.setContentType(sMimeType);
		
		//이 부분이 한글 파일명이 깨지는 것을 방지해 줍니다.
		String sEncoding = new String(postFileData.getPostFileOriginal().getBytes("utf-8"), "ISO-8859-1");
		
		/*
		 * Content-Disposition: attachment: 브라우저에서 다운로드하기 위해 사용됩니다.
		 */
		resp.setHeader("Content-Disposition", "attachment; filename="+sEncoding);
		
		try(	//웹 브라우저로서의 출력 스트림 생성합니다.
				BufferedOutputStream out2 = new BufferedOutputStream(resp.getOutputStream());
				
				//sFilePath로 지정한 파일에 대한 입력 스트림을 생성합니다.
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(sFilePath));
				){
			int numRead;
			//read(b, 0, b.length) : 바이트 배열 b의 0번 부터 b.length 크기 만큼 읽어옵니다.
			while((numRead = in.read(b,0,b.length)) != -1) {//읽을 데이터가 존재하는 경우
				//바이트 배열 b의 0번 부터 numRead크기 만큼 브라우저로 출력
				out2.write(b,0,numRead);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
