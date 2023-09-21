package dev.hieplp.library.payload.response.auth;

import dev.hieplp.library.common.entity.User;
import dev.hieplp.library.common.model.TokenModel;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class LoginResponse {
    private User user;
    private Set<Byte> roles;
    private TokenModel accessToken;
    private TokenModel refreshToken;
}
