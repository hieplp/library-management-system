package dev.hieplp.library.common.enums.user;

import lombok.Getter;

@Getter
public enum UserStatus {
    INACTIVE(0),
    ACTIVE(1),
    ;

    private final Byte status;

    UserStatus(Integer status) {
        this.status = status.byteValue();
    }


}
