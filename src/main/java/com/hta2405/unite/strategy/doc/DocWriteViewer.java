package com.hta2405.unite.strategy.doc;

import com.hta2405.unite.enums.DocType;
import org.springframework.ui.Model;

public interface DocWriteViewer {
    DocType getType();

    void prepareWriteView(String empId, Model model);

    String getView();
}
