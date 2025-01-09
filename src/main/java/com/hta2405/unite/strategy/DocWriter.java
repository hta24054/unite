package com.hta2405.unite.strategy;

import com.hta2405.unite.enums.DocType;
import org.springframework.ui.Model;

public interface DocWriter {
    DocType getType();

    void prepareWriter(String empId, Model model);

    String getView();
}
