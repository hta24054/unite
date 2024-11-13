package com.hta2405.unite.enums;

import lombok.Getter;

@Getter
public enum DocRole {
    WRITER("writer"), SIGNER("signer"), VIEWER("viewer");

    private final String type;

    DocRole(String type) {
        this.type = type;
    }

    public static DocRole fromString(String type) {
        for (DocRole docRole : DocRole.values()) {
            if (docRole.getType().equals(type)) {
                return docRole;
            }
        }
        throw new IllegalArgumentException("알수없는 타입명: " + type);
    }
}