package dev.hieplp.library.common.enums;

import dev.hieplp.library.common.exception.UnknownException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Nationality {
    UNKNOWN(0),
    VIETNAM(1),
    ;

    private final Byte value;

    Nationality(Integer value) {
        this.value = value.byteValue();
    }

    public static Nationality fromNationality(Byte nationality) {
        return Arrays.stream(values())
                .filter(value -> value.value.equals(nationality))
                .findFirst()
                .orElseThrow(() -> new UnknownException("Unknown nationality"));
    }
}
