package dev.hieplp.library.common.util;

import dev.hieplp.library.common.config.TokenConfig;
import dev.hieplp.library.common.entity.User;
import dev.hieplp.library.common.enums.token.TokenHeader;
import dev.hieplp.library.common.enums.token.TokenType;
import dev.hieplp.library.common.model.TokenModel;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenUtil {

    private final DateTimeUtil dateTimeUtil;

    public TokenModel generateToken(TokenConfig tokenConfig,
                                    PrivateKey privateKey,
                                    TokenType tokenType,
                                    User user) {
        log.info("Generating token with user: {}, tokenType: {} and tokenConfig: {}", user, tokenType, tokenConfig);


        // Expiration time
        var currentDate = dateTimeUtil.getCurrentDate();
        var expiredAt = dateTimeUtil.addSeconds(currentDate, tokenConfig.getActiveTime());

        // Headers
        var headers = new HashMap<String, Object>();
        headers.put(TokenHeader.USER_ID.getHeader(), user.getUserId());
        headers.put(TokenHeader.TOKEN_TYPE.getHeader(), tokenType.getType());

        // JWT builder
        var jwt = Jwts.builder()
                .setHeader(headers)
                .setSubject(String.format("%s|%s|%s", user.getUserId(), user.getEmail(), tokenType.getType()))
                .setAudience(user.getUserId())
                .setIssuer(tokenConfig.getIssuer())
                .setIssuedAt(currentDate)
                .setExpiration(expiredAt)
                .signWith(privateKey)
                .compact();

        return TokenModel.builder()
                .token(jwt)
                .expiredAt(expiredAt)
                .build();
    }
}
