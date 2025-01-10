package com.hta2405.unite.strategy.doc;

import com.hta2405.unite.domain.Doc;
import com.hta2405.unite.enums.DocRole;
import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.mybatis.mapper.AttendMapper;
import com.hta2405.unite.service.DocService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class VacationDocEditViewer implements DocEditViewer {

    private final DocService docService;
    private final AttendMapper attendMapper;

    @Override
    public DocType getType() {
        return DocType.VACATION;
    }

    @Override
    public void prepareEditView(Doc doc, DocRole docRole, Model model) {
        docService.addCommonReadAttrToModel(doc, docRole, model);
        model.addAttribute("docTrip", docService.getDocTripByDocId(doc.getDocId()));
        model.addAttribute("docVacation", docService.getDocVacationByDocId(doc.getDocId()));
        model.addAttribute("usedCount", attendMapper.getAnnualAppliedVacationCount(doc.getDocWriter(), LocalDate.now().getYear()));
    }

    @Override
    public String getView() {
        return "/doc/vacation_edit";
    }
}
