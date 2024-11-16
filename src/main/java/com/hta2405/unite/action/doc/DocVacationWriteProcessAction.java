package com.hta2405.unite.action.doc;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.DocDao;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.DocVacation;
import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.util.ConfigUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class DocVacationWriteProcessAction implements Action {
    private static final String UPLOAD_DIRECTORY = ConfigUtil.getProperty("vacation.upload.directory");
    private final DocDao docDao = new DocDao();
    private final EmpDao empDao = new EmpDao();

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 파라미터 수집
        String writer = req.getParameter("writer");
        String title = "휴가신청서(" + empDao.getEmpById(writer).getEname() + ")";
        String type = req.getParameter("type");
        String vacationStart = req.getParameter("vacation_start");
        String vacationEnd = req.getParameter("vacation_end");
        int vacationCount = Integer.parseInt(req.getParameter("vacation_count"));
        String content = req.getParameter("content");

        LocalDate vacationStartDate = LocalDate.parse(vacationStart);
        LocalDate vacationEndDate = LocalDate.parse(vacationEnd);

        String[] signers = req.getParameterValues("signers[]");

        // 파일 처리
        Part filePart = req.getPart("file");
        String filePath = null;
        String fileOriginalName = null;
        String fileUUID = null;
        String fileType = null;

        if (filePart != null && filePart.getSize() > 0) {
            // 파일 이름과 타입
            fileOriginalName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            fileType = filePart.getContentType();

            // UUID 생성하여 파일명에 적용
            fileUUID = UUID.randomUUID().toString();
            String fileName = fileUUID + "_" + fileOriginalName;

            // 저장 경로 설정 및 파일 저장
            filePath = UPLOAD_DIRECTORY + File.separator + fileName;
            File uploadDir = new File(UPLOAD_DIRECTORY);
            if (!uploadDir.exists()) {
                boolean dirsCreated = uploadDir.mkdirs();
                if (!dirsCreated) {
                    throw new IOException("업로드 폴더를 생성할 수 없습니다: " + UPLOAD_DIRECTORY);
                }
            }
            filePart.write(filePath);
        }

        // DocVacation 객체 생성
        DocVacation docVacation = new DocVacation(
                null,
                writer,
                DocType.VACATION,
                title,
                content,
                LocalDateTime.now(),
                false,
                LocalDate.now(),
                vacationStartDate,
                vacationEndDate,
                vacationCount,
                type,
                filePath,
                fileOriginalName,
                fileUUID,
                fileType
        );

        int result = docDao.insertVacationDoc(docVacation, List.of(signers));

        String status = result == 1 ? "success" : "fail";
        resp.setContentType("application/json");
        resp.getWriter().print("{\"status\":\"" + status + "\"}");

        return null;
    }
}
