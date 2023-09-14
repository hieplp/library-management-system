package dev.hieplp.library.common.enums.otp;

import lombok.Getter;

@Getter
public enum OtpType {
    UNKNOWN(-1),
    REGISTER(0),
    FORGOT_PASSWORD(1),
    VERIFY(2);

    private final Byte type;

    OtpType(Integer type) {
        this.type = type.byteValue();
    }
}
