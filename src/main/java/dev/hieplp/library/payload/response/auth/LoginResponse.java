package dev.hieplp.library.payload.response.auth;

import dev.hieplp.library.common.entity.User;
import dev.hieplp.library.common.model.TokenModel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private User user;
    private TokenModel accessToken;
    private TokenModel refreshToken;
}
