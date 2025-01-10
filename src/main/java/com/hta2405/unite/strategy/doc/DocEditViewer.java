package com.hta2405.unite.strategy.doc;

import com.hta2405.unite.domain.Doc;
import com.hta2405.unite.enums.DocRole;
import com.hta2405.unite.enums.DocType;
import org.springframework.ui.Model;

public interface DocEditViewer {
    DocType getType();

    void prepareEditView(Doc doc, DocRole docRole, Model model);

    String getView();
}
