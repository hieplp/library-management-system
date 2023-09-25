package dev.hieplp.library.common.enums.location;

import lombok.Getter;

@Getter
public enum WardStatus {
    INACTIVE(0),
    ACTIVE(1),
    ;

    private final Byte status;

    WardStatus(Integer status) {
        this.status = status.byteValue();
    }
}
