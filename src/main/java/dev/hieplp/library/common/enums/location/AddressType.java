package dev.hieplp.library.common.enums.location;

import dev.hieplp.library.common.exception.UnknownException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AddressType {
    HOME(0),
    WORK(1),
    SCHOOL(2),
    ;

    private final Byte type;

    AddressType(Integer type) {
        this.type = type.byteValue();
    }

    public static AddressType fromValue(Byte type) {
        return Arrays.stream(values())
                .filter(value -> value.type.equals(type))
                .findFirst()
                .orElseThrow(() -> new UnknownException("Unknown address type"));
    }
}
