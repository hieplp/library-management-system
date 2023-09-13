package dev.hieplp.library.common.enums.user;

import lombok.Getter;

@Getter
public enum UserRole {
    ROOT(0),
    ADMIN(1),
    USER(2),
    ;

    private final Byte role;

    UserRole(Integer role) {
        this.role = role.byteValue();
    }

    public String getRoleAsString() {
        return role.toString();
    }
}
