package dev.hieplp.library.common.enums.location;

import dev.hieplp.library.common.exception.UnknownException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AddressStatus {
    INACTIVE(0),
    ACTIVE(1),
    ;

    private final Byte status;

    AddressStatus(Integer type) {
        this.status = type.byteValue();
    }

    public static AddressStatus fromValue(Byte status) {
        return Arrays.stream(values())
                .filter(value -> value.status.equals(status))
                .findFirst()
                .orElseThrow(() -> new UnknownException("Unknown address status"));
    }
}
