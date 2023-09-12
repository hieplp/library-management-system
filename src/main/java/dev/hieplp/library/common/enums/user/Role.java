package dev.hieplp.library.common.enums.user;

import lombok.Getter;

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
}
