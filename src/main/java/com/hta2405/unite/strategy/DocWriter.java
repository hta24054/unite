package com.hta2405.unite.strategy;

import org.springframework.ui.Model;

public interface DocWriter {
    String getType();

    void prepareWriter(String empId, Model model);
}
