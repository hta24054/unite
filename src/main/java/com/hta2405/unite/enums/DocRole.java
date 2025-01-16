package com.hta2405.unite.enums;

import lombok.Getter;

@Getter
public enum DocRole {
    PRE_SIGNED_WRITER("preSignedWriter"),
    POST_SIGNED_WRITER("postSignedWriter"),
    PRE_SIGNER("preSigner"),
    POST_SIGNER("postSigner"),
    VIEWER("viewer"),
    INVALID("invalid");

    private final String type;

    DocRole(String type) {
        this.type = type;
    }
}