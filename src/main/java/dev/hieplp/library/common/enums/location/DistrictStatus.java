package dev.hieplp.library.common.enums.location;

import lombok.Getter;

@Getter
public enum DistrictStatus {
    INACTIVE(0),
    ACTIVE(1),
    ;

    private final Byte status;

    DistrictStatus(Integer status) {
        this.status = status.byteValue();
    }
}
