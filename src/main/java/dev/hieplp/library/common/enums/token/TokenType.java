package dev.hieplp.library.common.enums.token;

import lombok.Getter;

@Getter
public enum TokenType {
    ACCESS_TOKEN(0),
    REFRESH_TOKEN(1);

    private final Byte type;

    TokenType(Integer type) {
        this.type = type.byteValue();
    }
}
