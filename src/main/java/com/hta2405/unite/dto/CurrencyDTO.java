package com.hta2405.unite.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyDTO {
    @JsonProperty("cur_unit")
    private String curUnit;        // 통화 단위
    @JsonProperty("cur_nm")
    private String curNm;          // 통화명
    @JsonProperty("deal_bas_r")
    private String dealBasR;       // 기준 환율
}