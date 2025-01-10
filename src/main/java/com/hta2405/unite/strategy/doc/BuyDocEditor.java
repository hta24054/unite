package com.hta2405.unite.strategy.doc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hta2405.unite.domain.Doc;
import com.hta2405.unite.domain.DocTrip;
import com.hta2405.unite.dto.DocRequestDTO;
import com.hta2405.unite.dto.ProductDTO;
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
public class BuyDocEditor implements DocEditor {

    private final DocService docService;

    @Override
    public DocType getType() {
        return DocType.BUY;
    }

    @Override
    public void edit(Doc doc, DocRequestDTO req) {
        ObjectMapper mapper = new ObjectMapper();
        List<String> signers = mapper.convertValue(req.getFormData().get("signers"), new TypeReference<>() {
        });
        List<ProductDTO> products = mapper.convertValue(req.getFormData().get("products"), new TypeReference<>() {
        });

        Map<String, Object> formData = req.getFormData();
        Doc updateDoc = Doc.builder()
                .docId(doc.getDocId())
                .docWriter(doc.getDocWriter())
                .docType(DocType.BUY)
                .docTitle(formData.get("title").toString())
                .docContent(formData.get("content").toString())
                .docCreateDate(LocalDateTime.now())
                .signFinish(false).build();

        docService.updateBuyDoc(updateDoc, signers, products);
    }
}
