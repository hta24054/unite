package com.hta2405.unite.strategy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hta2405.unite.domain.Doc;
import com.hta2405.unite.domain.DocTrip;
import com.hta2405.unite.dto.DocSaveRequestDTO;
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
public class TripDocSaver implements DocSaver {
    private final DocService docService;

    @Override
    public String getType() {
        return "trip";
    }

    @Override
    public void save(String empId, DocSaveRequestDTO req) {
        Map<String, Object> formData = req.getFormData();
        Doc doc = Doc.builder().docWriter(empId)
                .docType(DocType.TRIP)
                .docTitle("출장신청서(" + formData.get("writer").toString() + ")")
                .docContent(formData.get("trip_info").toString())
                .docCreateDate(LocalDateTime.now())
                .signFinish(false).build();

        DocTrip docTrip = DocTrip.builder()
                .tripStart(LocalDate.parse(formData.get("trip_start").toString()))
                .tripEnd(LocalDate.parse(formData.get("trip_end").toString()))
                .tripLoc(formData.get("trip_loc").toString())
                .tripPhone(formData.get("trip_phone").toString())
                .tripInfo(formData.get("trip_info").toString())
                .cardStart(LocalDate.parse(formData.get("card_start").toString()))
                .cardEnd(LocalDate.parse(formData.get("card_end").toString()))
                .cardReturn(LocalDate.parse(formData.get("card_return").toString()))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        List<String> signers = mapper.convertValue(req.getFormData().get("signers"), new TypeReference<>() {
        });

        docService.saveTripDoc(doc, docTrip, signers);
    }
}
