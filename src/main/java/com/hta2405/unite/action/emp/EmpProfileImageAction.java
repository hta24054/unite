package com.hta2405.unite.action.emp;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.util.ConfigUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class EmpProfileImageAction implements Action {
    private static final String PROFILE_IMAGE_DIR = ConfigUtil.getProperty("profile.upload.directory");
    private static final String DEFAULT_PROFILE_IMAGE = "profile_navy_round.png"; // 기본 이미지 파일명
    private final EmpDao empDao = new EmpDao();

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileUUID = req.getParameter("UUID");
        String filePath;

        if (fileUUID == null || fileUUID.isEmpty()) {
            // UUID가 없을 경우 기본 이미지 반환
            filePath = PROFILE_IMAGE_DIR + File.separator + DEFAULT_PROFILE_IMAGE;
            System.out.println(filePath);
        } else {
            // UUID로 파일 찾기 (예: DB 조회)
            String originalFileName = getFileNameByUUID(fileUUID);
            if (originalFileName == null) {
                // 프로필 이미지가 없으면 기본 이미지 반환
                filePath = PROFILE_IMAGE_DIR + File.separator + DEFAULT_PROFILE_IMAGE;
                System.out.println(filePath);
            } else {
                // 실제 이미지 파일 경로 설정
                filePath = PROFILE_IMAGE_DIR + File.separator + fileUUID + "_" + originalFileName;
            }
        }

        File imageFile = new File(filePath);

        if (!imageFile.exists()) {
            // 기본 이미지가 없는 경우 에러 반환
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "기본 이미지를 찾을 수 없습니다.");
            return null;
        }

        // MIME 타입 설정
        String mimeType = req.getServletContext().getMimeType(imageFile.getName());
        if (mimeType == null) {
            mimeType = "application/octet-stream"; // 기본 MIME 타입
        }
        resp.setContentType(mimeType);
        resp.setHeader("Cache-Control", "public, max-age=86400");

        // 파일 스트리밍
        try (FileInputStream inStream = new FileInputStream(imageFile);
             OutputStream outStream = resp.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        }
        return null;
    }

    private String getFileNameByUUID(String fileUUID) {
        return empDao.getImgOriginal(fileUUID);
    }
}