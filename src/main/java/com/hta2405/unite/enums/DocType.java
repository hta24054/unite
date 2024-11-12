package com.hta2405.unite.enums;

public enum DocType {
    VACATION("doc_vac"), BUY("doc_buy"), TRIP("doc_trip"), GENERAL("doc");

    private final String type;

    DocType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}