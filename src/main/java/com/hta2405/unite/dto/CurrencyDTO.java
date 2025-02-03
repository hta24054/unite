package com.hta2405.unite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyDTO {
    private String curUnit;        // 통화 단위
    private String curValue;       // 기준 환율
}