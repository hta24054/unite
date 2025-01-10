package com.hta2405.unite.strategy.doc;

import com.hta2405.unite.dto.DocRequestDTO;
import com.hta2405.unite.enums.DocType;

public interface DocWriter {
    DocType getType();

    void write(String empId, DocRequestDTO req);
}