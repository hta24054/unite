package com.hta2405.unite.strategy.doc;

import com.hta2405.unite.domain.BuyItem;
import com.hta2405.unite.domain.Doc;
import com.hta2405.unite.enums.DocRole;
import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.service.DocService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BuyDocReader implements DocReader {

    private final DocService docService;

    @Override
    public DocType getType() {
        return DocType.BUY;
    }

    @Override
    public void prepareRead(Doc doc, DocRole docRole, Model model) {
        docService.addCommonReadAttrToModel(doc, docRole, model);
        List<BuyItem> itemList = docService.getBuyItemListByDocId(doc.getDocId());
        model.addAttribute("itemList", itemList);
    }

    @Override
    public String getView() {
        return "doc/buy_read";
    }
}
