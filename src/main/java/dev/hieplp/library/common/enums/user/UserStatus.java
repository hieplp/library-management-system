package dev.hieplp.library.common.enums.user;

import dev.hieplp.library.common.exception.UnknownException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum UserStatus {
    INACTIVE(0),
    ACTIVE(1),
    NOT_VERIFIED(2),
    ;

    private final Byte status;

    UserStatus(Integer status) {
        this.status = status.byteValue();
    }


    public static UserStatus fromStatus(Byte status) {
        return Arrays.stream(values())
                .filter(value -> value.status.equals(status))
                .findFirst()
                .orElseThrow(() -> new UnknownException("Unknown user status"));
    }
}
