package dev.hieplp.library.payload.request.user;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String fullName;
    private Byte status;
}
