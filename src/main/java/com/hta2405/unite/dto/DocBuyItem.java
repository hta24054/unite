package com.hta2405.unite.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DocBuyItem extends Doc {
    private Long buyItemId;
    private Long docBuyId;
    private String productName;
    private String standard;
    private Long quantity;
    private Long price;
}
