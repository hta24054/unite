package com.hta2405.unite.enums;

import lombok.Getter;

@Getter
public enum DocRole {
    PRE_SIGNED_WRITER("preSignedWriter"),
    POST_SIGNED_WRITER("postSignedWriter"),
    PRE_SIGNER("preSigner"),
    POST_SIGNER("postSigner"),
    VIEWER("viewer");

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