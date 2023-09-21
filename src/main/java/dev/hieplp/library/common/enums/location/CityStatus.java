package dev.hieplp.library.common.enums.location;

import lombok.Getter;

@Getter
public enum CityStatus {
    INACTIVE(0),
    ACTIVE(1),
    ;

    private final Byte status;

    CityStatus(Integer status) {
        this.status = status.byteValue();
    }
}
