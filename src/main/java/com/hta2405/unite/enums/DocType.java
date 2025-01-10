package com.hta2405.unite.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DocType {
    VACATION("휴가신청서", "vacation"),
    BUY("구매신청서", "buy"),
    TRIP("출장신청서", "trip"),
    GENERAL("일반문서", "general");

    private final String typeKor;
    private final String typeEng;

    public static DocType fromString(String type) {
        for (DocType docType : DocType.values()) {
            if (docType.getTypeKor().equalsIgnoreCase(type) ||
                    docType.getTypeEng().equalsIgnoreCase(type)) {
                return docType;
            }
        }
        throw new IllegalArgumentException("알 수 없는 타입명: " + type);
    }
}