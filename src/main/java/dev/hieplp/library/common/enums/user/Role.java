package dev.hieplp.library.common.enums.user;

import dev.hieplp.library.common.exception.UnknownException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Role {
    ROOT(0),
    ADMIN(1),
    USER(2),
    ;

    private final Byte role;

    Role(Integer role) {
        this.role = role.byteValue();
    }

    public static Role fromRole(Byte role) {
        return Arrays.stream(values())
                .filter(value -> value.role.equals(role))
                .findFirst()
                .orElseThrow(() -> new UnknownException("Unknown user role"));
    }

    public String getRoleAsString() {
        return role.toString();
    }

}
