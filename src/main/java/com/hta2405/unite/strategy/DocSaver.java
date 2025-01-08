package com.hta2405.unite.strategy;

import com.hta2405.unite.dto.DocSaveRequestDTO;

public interface DocSaver {
    String getType();

    void save(String empId, DocSaveRequestDTO req);
}