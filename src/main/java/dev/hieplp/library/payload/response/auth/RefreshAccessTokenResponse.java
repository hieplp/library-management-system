package dev.hieplp.library.payload.response.auth;

import dev.hieplp.library.common.model.TokenModel;
import lombok.Data;

@Data
public class RefreshAccessTokenResponse {
    private TokenModel accessToken;
}
