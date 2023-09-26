package dev.hieplp.library.common.enums;

import lombok.Getter;

@Getter
public enum IdLength {
    USER_ID(10),
    OTP_ID(10),
    OTP_HISTORY_ID(10),
    CATALOG_ID(10),
    ADDRESS_ID(10),
    ;

    private final int length;

    IdLength(int length) {
        this.length = length;
    }
}
