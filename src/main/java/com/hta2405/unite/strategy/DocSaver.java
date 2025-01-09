package com.hta2405.unite.strategy;

import com.hta2405.unite.dto.DocSaveRequestDTO;
import com.hta2405.unite.enums.DocType;

public interface DocSaver {
    DocType getType();

    void save(String empId, DocSaveRequestDTO req);
}