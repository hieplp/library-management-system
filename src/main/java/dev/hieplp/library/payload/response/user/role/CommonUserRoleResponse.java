package dev.hieplp.library.payload.response.user.role;

import lombok.Data;

@Data
public class CommonUserRoleResponse {
    private String userId;
    private Byte role;
}
