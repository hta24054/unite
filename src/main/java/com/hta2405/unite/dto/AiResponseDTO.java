package com.hta2405.unite.dto;

import lombok.Data;

import java.util.Map;

@Data
public class AiResponseDTO {
    private String typeName;
    private Map<String, Object> response;
}
