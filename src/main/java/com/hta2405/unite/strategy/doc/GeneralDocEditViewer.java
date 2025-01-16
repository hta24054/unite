package com.hta2405.unite.strategy.doc;

import com.hta2405.unite.domain.Doc;
import com.hta2405.unite.enums.DocRole;
import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.service.DocService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
@RequiredArgsConstructor
public class GeneralDocEditViewer implements DocEditViewer {

    private final DocService docService;

    @Override
    public DocType getType() {
        return DocType.GENERAL;
    }

    @Override
    public void prepareEditView(Doc doc, DocRole docRole, Model model) {
        docService.addCommonReadAttrToModel(doc, docRole, model);
    }

    @Override
    public String getView() {
        return "/doc/general_edit";
    }
}
