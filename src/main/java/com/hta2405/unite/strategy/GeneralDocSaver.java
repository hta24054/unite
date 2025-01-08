package com.hta2405.unite.strategy;

import com.hta2405.unite.domain.Doc;
import com.hta2405.unite.domain.Sign;
import com.hta2405.unite.dto.DocSaveRequestDTO;
import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.service.DocService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GeneralDocSaver implements DocSaver {
    private final DocService docService;

    @Override
    public String getType() {
        return "general";
    }

    @Override
    public void save(String empId, DocSaveRequestDTO request) {
        Doc doc = Doc.builder().docWriter(empId)
                .docType(DocType.GENERAL)
                .docTitle(request.getFormData().get("title").toString())
                .docContent(request.getFormData().get("content").toString())
                .docCreateDate(LocalDateTime.now())
                .signFinish(false).build();
        docService.saveGeneralDoc(doc, request.getSigners());
    }
}
