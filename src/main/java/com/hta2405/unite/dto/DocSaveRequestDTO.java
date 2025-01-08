package com.hta2405.unite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class DocSaveRequestDTO {
    private Map<String, Object> formData;
    private List<MultipartFile> files;
}
