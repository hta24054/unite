package com.hta2405.unite.strategy.doc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hta2405.unite.domain.Doc;
import com.hta2405.unite.domain.DocVacation;
import com.hta2405.unite.dto.DocRequestDTO;
import com.hta2405.unite.enums.AttendType;
import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.service.DocService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class VacationDocEditor implements DocEditor {

    private final DocService docService;

    @Override
    public DocType getType() {
        return DocType.VACATION;
    }

    @Override
    public void edit(Doc doc, DocRequestDTO req) {
        ObjectMapper mapper = new ObjectMapper();
        List<String> signers = mapper.convertValue(req.getFormData().get("signers"), new TypeReference<>() {
        });

        Map<String, Object> formData = req.getFormData();
        DocVacation docVacation = docService.getDocVacationByDocId(doc.getDocId());
        Doc updateDoc = Doc.builder()
                .docId(doc.getDocId())
                .docWriter(doc.getDocWriter())
                .docType(this.getType())
                .docTitle("휴가신청서(" + formData.get("writer").toString() + ")")
                .docContent(formData.get("content").toString())
                .docCreateDate(LocalDateTime.now())
                .signFinish(false).build();

        DocVacation.DocVacationBuilder docVacationBuilder = DocVacation.builder()
                .docId(doc.getDocId())
                .vacationStart(LocalDate.parse(formData.get("vacation_start").toString()))
                .vacationEnd(LocalDate.parse(formData.get("vacation_end").toString()))
                .vacationCount(Integer.parseInt(formData.get("vacation_count").toString()))
                .vacationType(AttendType.fromString((formData.get("vacation_type").toString())))
                .vacationFileOriginal(docVacation.getVacationFileOriginal())
                .vacationFilePath(docVacation.getVacationFilePath())
                .vacationFileUUID(docVacation.getVacationFileUUID())
                .vacationFileType(docVacation.getVacationFileType());


        List<MultipartFile> files = req.getFiles();
        boolean fileChange = formData.get("fileChange").toString().equals("true");

        docService.updateVacationDoc(updateDoc, docVacation, docVacationBuilder, signers, files, fileChange);
    }
}
