package com.hta2405.unite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockIndexDTO {
    private double kospi;        // 코스피 지수
    private double kosdaq;       // 코스닥 지수
}