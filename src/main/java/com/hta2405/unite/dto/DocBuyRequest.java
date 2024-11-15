package com.hta2405.unite.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class DocBuyRequest {
    private Long docId;
    private Long docBuyId;
    private String writer;
    private String title;
    private String content;
    private List<String> signers;
    private List<Product> productDetails;

    @Getter
    public static class Product {
        //TODO GSON 파싱 문제로 snake case, 해결방법 찾는중
        private String product_name;
        private String standard;
        private long quantity;
        private long price;
    }
}
