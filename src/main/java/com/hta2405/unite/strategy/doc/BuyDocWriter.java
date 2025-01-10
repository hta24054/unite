package com.hta2405.unite.strategy.doc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hta2405.unite.domain.Doc;
import com.hta2405.unite.dto.DocRequestDTO;
import com.hta2405.unite.dto.ProductDTO;
import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.service.DocService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BuyDocWriter implements DocWriter {
    private final DocService docService;

    @Override
    public DocType getType() {
        return DocType.BUY;
    }

    @Override
    public void write(String empId, DocRequestDTO req) {
        ObjectMapper mapper = new ObjectMapper();
        List<String> signers = mapper.convertValue(req.getFormData().get("signers"), new TypeReference<>() {
        });
        List<ProductDTO> products = mapper.convertValue(req.getFormData().get("products"), new TypeReference<>() {
        });

        Doc doc = Doc.builder().docWriter(empId)
                .docType(DocType.BUY)
                .docTitle(req.getFormData().get("title").toString())
                .docContent(req.getFormData().get("content").toString())
                .docCreateDate(LocalDateTime.now())
                .signFinish(false).build();
        docService.saveBuyDoc(doc, signers, products);
    }
}
