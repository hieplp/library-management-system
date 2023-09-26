package dev.hieplp.library.common.enums.location;

import lombok.Getter;

@Getter
public enum AddressDefault {
    NO(0),
    YES(1),
    ;

    private final Byte status;

    AddressDefault(Integer type) {
        this.status = type.byteValue();
    }

}
