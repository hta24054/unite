package com.hta2405.unite.action.project;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.util.ConfigUtil;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//필터 사용하지 않으면 ListAction2로 해야되는데
//필터를 사용하면 막을 수 있다
//필터가 없다면 그냥 접속 가능하기에 그렇게 하면 안된다
public class ProjectDownAction implements Action {
	private static final String UPLOAD_DIRECTORY = ConfigUtil.getProperty("project.upload.directory");
	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    // uuid+type 형식의 파일명을 요청으로부터 받음
	    String fileName = request.getParameter("filename");
	    System.out.println("filename = " + fileName);
	    
	    // 원본 파일명 (다운로드할 때 표시할 이름)으로 다운되게 하기 위해
	    String originalFileName = request.getParameter("originalFilename");
	    System.out.println("Original filename = " + originalFileName);

	    String savePath = "projectupload";
	    ServletContext context = request.getServletContext();
	    String sDownloadPath = context.getRealPath(savePath);

	    String sFilePath = UPLOAD_DIRECTORY + File.separator + fileName;
	    System.out.println("File path: " + sFilePath);

	    byte[] b = new byte[4096];
	    String sMimeType = context.getMimeType(sFilePath);
	    if (sMimeType == null) {
	        sMimeType = "application/octet-stream";
	    }
	    response.setContentType(sMimeType);

	    // 원본 파일명으로 다운로드 되도록 설정
	    String sEncoding = new String(originalFileName.getBytes("utf-8"), "ISO-8859-1");
	    response.setHeader("Content-Disposition", "attachment; filename=" + sEncoding);

	    try (BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
	         BufferedInputStream in = new BufferedInputStream(new FileInputStream(sFilePath))) {
	        int numRead;
	        while ((numRead = in.read(b, 0, b.length)) != -1) {
	            out.write(b, 0, numRead);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    // 다운로드 완료 후 이동할 페이지가 없으므로, null로 설정된 ActionForward 반환
	    return null;
	}


}


















