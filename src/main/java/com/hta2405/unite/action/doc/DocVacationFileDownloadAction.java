package com.hta2405.unite.action.doc;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.util.CommonUtil;
import com.hta2405.unite.util.ConfigUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class DocVacationFileDownloadAction implements Action {
    private static final String UPLOAD_DIRECTORY = ConfigUtil.getProperty("vacation.upload.directory");

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileName = req.getParameter("fileName");
        fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
        String fileUUID = req.getParameter("fileUUID");

        // 실제 파일 경로
        String filePath = UPLOAD_DIRECTORY + File.separator + fileUUID + "_" + fileName;
        System.out.println("실제 파일 경로: " + filePath);

        File downloadFile = new File(filePath);
        if (!downloadFile.exists()) {
            return CommonUtil.alertAndGoBack(resp, "파일을 찾을 수 없습니다.");
        }

        String sEncoding = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + sEncoding + "\"");

        try (FileInputStream inStream = new FileInputStream(downloadFile);
             OutputStream outStream = resp.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            return CommonUtil.alertAndGoBack(resp, "파일 다운로드 중 오류가 발생했습니다.");
        }

        return null;
    }
}
