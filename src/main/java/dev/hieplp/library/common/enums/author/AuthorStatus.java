package dev.hieplp.library.common.enums.author;

import dev.hieplp.library.common.exception.UnknownException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AuthorStatus {
    INACTIVE(0),
    ACTIVE(1),
    ;

    private final Byte status;

    AuthorStatus(Integer status) {
        this.status = status.byteValue();
    }

    public static AuthorStatus fromStatus(Byte status) {
        return Arrays.stream(values())
                .filter(value -> value.status.equals(status))
                .findFirst()
                .orElseThrow(() -> new UnknownException("Unknown author status"));
    }
}
