package com.hta2405.unite.strategy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hta2405.unite.domain.Doc;
import com.hta2405.unite.domain.DocVacation;
import com.hta2405.unite.dto.DocSaveRequestDTO;
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
public class VacationDocSaver implements DocSaver {
    private final DocService docService;

    @Override
    public DocType getType() {
        return DocType.VACATION;
    }

    @Override
    public void save(String empId, DocSaveRequestDTO req) {
        Map<String, Object> formData = req.getFormData();
        Doc doc = Doc.builder().docWriter(empId)
                .docType(DocType.VACATION)
                .docTitle("휴가신청서(" + formData.get("writer").toString() + ")")
                .docContent(formData.get("content").toString())
                .docCreateDate(LocalDateTime.now())
                .signFinish(false).build();

        ObjectMapper mapper = new ObjectMapper();
        List<String> signers = mapper.convertValue(req.getFormData().get("signers"), new TypeReference<>() {
        });
        List<MultipartFile> files = req.getFiles();

        DocVacation.DocVacationBuilder docVacationBuilder = DocVacation.builder()
                .vacationApply(LocalDate.now())
                .vacationStart(LocalDate.parse(formData.get("vacation_start").toString()))
                .vacationEnd(LocalDate.parse(formData.get("vacation_end").toString()))
                .vacationCount(Integer.parseInt(formData.get("vacation_count").toString()))
                .vacationType(AttendType.fromString((formData.get("vacation_type").toString())));

        docService.saveVacationDoc(doc, docVacationBuilder, signers, files);
    }
}
