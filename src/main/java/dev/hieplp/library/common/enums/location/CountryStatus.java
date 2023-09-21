package dev.hieplp.library.common.enums.location;

import lombok.Getter;

@Getter
public enum CountryStatus {
    INACTIVE(0),
    ACTIVE(1),
    ;

    private final Byte status;

    CountryStatus(Integer status) {
        this.status = status.byteValue();
    }
}
