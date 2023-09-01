package dev.hieplp.library.common.enums.token;

import lombok.Getter;

@Getter
public enum TokenHeader {
    USER_ID("userId"),
    TOKEN_TYPE("tokenType"),
    ;

    private final String header;

    TokenHeader(String header) {
        this.header = header;
    }
}
