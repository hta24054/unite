package com.hta2405.unite.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class DocBuy {
    private Long docBuyId;
    private Long docId;

}
