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

public class DocVacationWriteAndEditProcessAction implements Action {
    private static final String UPLOAD_DIRECTORY = ConfigUtil.getProperty("vacation.upload.directory");
    private final DocDao docDao = new DocDao();
    private final EmpDao empDao = new EmpDao();

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Long docId = req.getParameter("docId") != null ?
                Long.parseLong(req.getParameter("docId")) : null;
        Long docVacationId = req.getParameter("docVacationId") != null ?
                Long.parseLong(req.getParameter("docVacationId")) : null;
        String writer = req.getParameter("writer");
        String title = "휴가신청서(" + empDao.getEmpById(writer).getEname() + ")";
        String type = req.getParameter("type");
        String vacationStart = req.getParameter("vacation_start");
        String vacationEnd = req.getParameter("vacation_end");
        int vacationCount = Integer.parseInt(req.getParameter("vacation_count"));
        String content = req.getParameter("content");


        LocalDate vacationStartDate = LocalDate.parse(vacationStart);
        LocalDate vacationEndDate = LocalDate.parse(vacationEnd);


        // 파일 처리
        Part filePart = req.getPart("file");
        String filePath = null;
        String fileOriginalName = null;
        String fileUUID = null;
        String fileType = null;

        //beforeFileName 파라미터가 넘어온 것은 파일 수정 안한 것
        String beforeFileName = req.getParameter("beforeFileName");
        if (beforeFileName != null) {
            DocVacation beforeDoc = docDao.getVacationDoc(docId);
            filePath = beforeDoc.getVacationFilePath();
            fileOriginalName = beforeDoc.getVacationFileOriginal();
            fileUUID = beforeDoc.getVacationFileUUID();
            fileType = beforeDoc.getVacationFileType();
        } else if (filePart != null && filePart.getSize() > 0) {
            // 파일 이름과 타입
            fileOriginalName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            fileType = filePart.getContentType();
            System.out.println(fileType);

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
                docId,
                writer,
                DocType.VACATION,
                title,
                content,
                LocalDateTime.now(),
                false,
                docVacationId,
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
        System.out.println(docVacationId);

        List<String> signList = List.of(req.getParameterValues("signers[]"));

        //처음 생성한 문서는 docId null이기 때문에
        if (docVacation.getDocId() == null) {
            insertVacationDoc(docVacation, signList, resp);
        } else {
            updateVacationDoc(docVacation, signList, resp);
        }
        return null;
    }

    private void insertVacationDoc(DocVacation docVacation, List<String> signList, HttpServletResponse resp) throws IOException {
        int result = docDao.insertVacationDoc(docVacation, signList);

        String status = result == 1 ? "success" : "fail";
        resp.setContentType("application/json");
        resp.getWriter().print("{\"status\":\"" + status + "\"}");
    }

    private void updateVacationDoc(DocVacation docVacation, List<String> signList, HttpServletResponse resp) throws IOException {
        //파일이 변경되었는지, 아닌지에 따라 수정 여부 달라짐
        int result = docDao.updateVacationDoc(docVacation, signList);

        String status = result == 1 ? "success" : "fail";
        resp.setContentType("application/json");
        resp.getWriter().print("{\"status\":\"" + status + "\"}");
    }
}
