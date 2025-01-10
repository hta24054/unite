package com.hta2405.unite.strategy.doc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hta2405.unite.domain.Doc;
import com.hta2405.unite.domain.DocTrip;
import com.hta2405.unite.dto.DocRequestDTO;
import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.service.DocService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TipDocEditor implements DocEditor {

    private final DocService docService;

    @Override
    public DocType getType() {
        return DocType.TRIP;
    }

    @Override
    public void edit(Doc doc, DocRequestDTO req) {
        ObjectMapper mapper = new ObjectMapper();
        List<String> signers = mapper.convertValue(req.getFormData().get("signers"), new TypeReference<>() {
        });

        Map<String, Object> formData = req.getFormData();

        Doc updateDoc = Doc.builder()
                .docId(Long.parseLong(formData.get("docId").toString()))
                .docWriter(doc.getDocWriter())
                .docType(this.getType())
                .docTitle("출장신청서(" + formData.get("writer").toString() + ")")
                .docContent(formData.get("trip_info").toString())
                .docCreateDate(LocalDateTime.now())
                .signFinish(false).build();

        DocTrip docTrip = DocTrip.builder()
                .docId(updateDoc.getDocId())
                .tripStart(LocalDate.parse(formData.get("trip_start").toString()))
                .tripEnd(LocalDate.parse(formData.get("trip_end").toString()))
                .tripLoc(formData.get("trip_loc").toString())
                .tripPhone(formData.get("trip_phone").toString())
                .tripInfo(formData.get("trip_info").toString())
                .cardStart(LocalDate.parse(formData.get("card_start").toString()))
                .cardEnd(LocalDate.parse(formData.get("card_end").toString()))
                .cardReturn(LocalDate.parse(formData.get("card_return").toString()))
                .build();

        docService.updateTripDoc(doc, docTrip, signers);
    }
}
