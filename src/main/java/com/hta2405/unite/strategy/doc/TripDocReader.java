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
public class TripDocReader implements DocReader {

    private final DocService docService;

    @Override
    public DocType getType() {
        return DocType.TRIP;
    }

    @Override
    public void prepareRead(Doc doc, DocRole docRole, Model model) {
        docService.addCommonReadAttrToModel(doc, docRole, model);
        model.addAttribute("docTrip", docService.getDocTripByDocId(doc.getDocId()));
    }

    @Override
    public String getView() {
        return "doc/trip_read";
    }
}
