package com.hta2405.unite.strategy.doc;

import com.hta2405.unite.domain.Doc;
import com.hta2405.unite.dto.DocRequestDTO;
import com.hta2405.unite.enums.DocType;

public interface DocEditor {
    DocType getType();

    void edit(Doc doc, DocRequestDTO req);
}
