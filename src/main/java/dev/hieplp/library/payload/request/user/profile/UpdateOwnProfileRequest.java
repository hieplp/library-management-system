package dev.hieplp.library.payload.request.user.profile;

import lombok.Data;

@Data
public class UpdateOwnProfileRequest {
    private String fullName;
    private String avatar;
}
