package dev.hieplp.library.common.enums.otp;

import lombok.Getter;

@Getter
public enum OtpStatus {
    PENDING(0),
    USED(1),
    EXPIRED(2);

    private final Byte status;

    OtpStatus(Integer status) {
        this.status = status.byteValue();
    }
}
