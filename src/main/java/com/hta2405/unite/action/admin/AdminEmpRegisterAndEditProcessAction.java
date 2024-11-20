package com.hta2405.unite.action.admin;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Cert;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.dto.Lang;
import com.hta2405.unite.util.ConfigUtil;
import com.hta2405.unite.util.EmpUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdminEmpRegisterAndEditProcessAction implements Action {
    private static final String UPLOAD_DIRECTORY = ConfigUtil.getProperty("profile.upload.directory");
    private final EmpDao empDao = new EmpDao();

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String empId = req.getParameter("empId");
        Emp existEmp = empDao.getEmpById(empId);
        boolean newRegister = existEmp == null;

        Long vacCount = newRegister ? 0 : existEmp.getVacationCount();
        String password = newRegister ? EmpUtil.hashingPassword(empId) : existEmp.getPassword();

        // 파일 처리
        Part filePart = req.getPart("file");
        String filePath = null;
        String fileOriginalName = null;
        String fileUUID = null;
        String fileType = null;

        String beforeFileName = req.getParameter("beforeFileName");

        if (beforeFileName != null) {
            // 파일을 수정하지 않은 경우 기존 파일 정보 유지
            Emp emp = empDao.getEmpById(empId);
            filePath = emp.getImgPath();
            fileOriginalName = emp.getImgOriginal();
            fileUUID = emp.getImgUUID();
            fileType = emp.getImgType();
        } else if (filePart != null && filePart.getSize() > 0) {
            // 새 파일 처리
            fileOriginalName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            fileType = filePart.getContentType();

            // UUID 생성
            fileUUID = UUID.randomUUID().toString();
            String fileName = fileUUID + "_" + fileOriginalName;

            // 저장 경로 설정
            String uploadPath = UPLOAD_DIRECTORY;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists() && !uploadDir.mkdirs()) {
                throw new IOException("업로드 폴더를 생성할 수 없습니다: " + uploadPath);
            }

            // 파일 저장
            File file = new File(uploadPath, fileName);
            filePart.write(file.getAbsolutePath());

            // 웹 경로 설정
            filePath = UPLOAD_DIRECTORY;
        }

        Emp emp = new Emp(
                empId,
                password,
                req.getParameter("ename"),
                Long.parseLong(req.getParameter("deptId")),
                Long.parseLong(req.getParameter("jobId")),
                req.getParameter("gender"),
                req.getParameter("email"),
                req.getParameter("tel"),
                req.getParameter("mobile"),
                req.getParameter("mobile2"),
                filePath,
                fileOriginalName,
                fileUUID,
                fileType,
                LocalDate.parse(req.getParameter("hireDate")),
                req.getParameter("hireType"),
                LocalDate.parse(req.getParameter("birthday")),
                req.getParameter("birthday_type"),
                req.getParameter("school"),
                req.getParameter("major"),
                req.getParameter("bank"),
                req.getParameter("account"),
                req.getParameter("address"),
                Integer.parseInt(req.getParameter("married")) == 1,
                Integer.parseInt(req.getParameter("child")) == 1,
                req.getParameter("etype"),
                vacCount,
                true);
        System.out.println("emp = " + emp);

        String[] langs = req.getParameterValues("lang");
        List<Lang> langList = new ArrayList<>();
        if (langs != null) {
            for (String lang : langs) {
                langList.add(new Lang(null, lang, empId));
            }
        }

        String[] certs = req.getParameterValues("cert");
        List<Cert> certList = new ArrayList<>();
        if (certs != null) {
            for (String cert : certs) {
                certList.add(new Cert(null, cert, empId));
            }
        }

        if (newRegister) {
            insertEmp(emp, certList, langList, resp);
        } else {
            updateEmp(emp, certList, langList, resp);
        }
        return null;
    }

    private void insertEmp(Emp emp, List<Cert> certList, List<Lang> langList, HttpServletResponse resp) throws IOException {
        int result = empDao.insertEmp(emp, certList, langList);
        String status = result == 1 ? "success" : "fail";
        resp.setContentType("application/json");
        resp.getWriter().print("{\"status\":\"" + status + "\"}");
    }

    private void updateEmp(Emp emp, List<Cert> certList, List<Lang> langList, HttpServletResponse resp) throws IOException {
        int result = empDao.updateEmp(emp, certList, langList);
        String status = result == 1 ? "success" : "fail";
        resp.setContentType("application/json");
        resp.getWriter().print("{\"status\":\"" + status + "\"}");
    }
}
