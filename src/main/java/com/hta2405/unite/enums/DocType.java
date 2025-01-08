package com.hta2405.unite.enums;

import lombok.Getter;

@Getter
public enum DocType {
    VACATION("휴가신청서"), BUY("구매신청서"), TRIP("출장신청서"), GENERAL("일반문서");

    private final String type;

    DocType(String type) {
        this.type = type;
    }

    public static DocType fromString(String type) {
        for (DocType docType : DocType.values()) {
            if (docType.getType().equals(type)) {
                return docType;
            }
        }
        throw new IllegalArgumentException("알수없는 타입명: " + type);
    }
}