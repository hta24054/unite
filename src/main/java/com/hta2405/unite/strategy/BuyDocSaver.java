package com.hta2405.unite.strategy;

import com.hta2405.unite.dto.DocSaveRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BuyDocSaver implements DocSaver {
    @Override
    public String getType() {
        return "buy";
    }

    @Override
    public void save(String empId, DocSaveRequestDTO docSaveRequestDTO) {

    }
}
