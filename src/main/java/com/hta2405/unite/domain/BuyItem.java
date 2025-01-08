package com.hta2405.unite.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class BuyItem {
    private Long buyItemId;
    private Long docBuyId;
    private String productName;
    private String standard;
    private Long quantity;
    private Long price;
}
