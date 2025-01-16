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
public class BuyDocEditViewer implements DocEditViewer {

    private final DocService docService;

    @Override
    public DocType getType() {
        return DocType.BUY;
    }

    @Override
    public void prepareEditView(Doc doc, DocRole docRole, Model model) {
        docService.addCommonReadAttrToModel(doc, docRole, model);
        model.addAttribute("itemList", docService.getBuyItemListByDocId(doc.getDocId()));
    }

    @Override
    public String getView() {
        return "/doc/buy_edit";
    }
}