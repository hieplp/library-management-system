package dev.hieplp.library.common.enums.user;

import lombok.Getter;

@Getter
public enum TempUserStatus {
    PENDING(0),
    VERIFIED(1),
    ;

    private final Byte status;

    TempUserStatus(Integer status) {
        this.status = status.byteValue();
    }

}