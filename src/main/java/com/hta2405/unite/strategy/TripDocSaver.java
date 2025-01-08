package com.hta2405.unite.strategy;

import com.hta2405.unite.dto.DocSaveRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TripDocSaver implements DocSaver {

    @Override
    public String getType() {
        return "trip";
    }

    @Override
    public void save(String empId, DocSaveRequestDTO docSaveRequestDTO) {

    }
}
