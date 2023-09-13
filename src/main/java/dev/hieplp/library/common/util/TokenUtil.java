package dev.hieplp.library.common.util;

import dev.hieplp.library.common.config.TokenConfig;
import dev.hieplp.library.common.entity.User;
import dev.hieplp.library.common.enums.token.TokenHeader;
import dev.hieplp.library.common.enums.token.TokenType;
import dev.hieplp.library.common.exception.UnauthorizedException;
import dev.hieplp.library.common.model.TokenModel;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenUtil {

    private final DateTimeUtil dateTimeUtil;

    public TokenModel generateToken(TokenConfig tokenConfig,
                                    PrivateKey privateKey,
                                    TokenType tokenType,
                                    User user,
                                    Set<Byte> roles) {
        log.info("Generating token with user: {}, tokenType: {} and tokenConfig: {}", user, tokenType, tokenConfig);

        // Expiration time
        var currentDate = dateTimeUtil.getCurrentDate();
        var expiredAt = dateTimeUtil.addSeconds(currentDate, tokenConfig.getActiveTime());

        // Headers
        var headers = new HashMap<String, Object>();
        headers.put(TokenHeader.USER_ID.getHeader(), user.getUserId());
        headers.put(TokenHeader.TOKEN_TYPE.getHeader(), tokenType.getType());

        // Roles
        if (roles != null && !roles.isEmpty()) {
            headers.put(TokenHeader.ROLES.getHeader(), roles);
        }

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

    public TokenModel generateToken(TokenConfig tokenConfig,
                                    PrivateKey privateKey,
                                    TokenType tokenType,
                                    User user) {
        return generateToken(tokenConfig, privateKey, tokenType, user, null);
    }

    public Jws<Claims> parseToken(String token, PrivateKey privateKey) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(privateKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException ex) {
            log.warn("JWT expired: {}", ex.getMessage());
            throw new UnauthorizedException("JWT expired");
        } catch (IllegalArgumentException ex) {
            log.warn("Token is null, empty or only whitespace: {}", ex.getMessage());
            throw new UnauthorizedException("Token is null, empty or only whitespace");
        } catch (MalformedJwtException ex) {
            log.warn("JWT is invalid", ex);
            throw new UnauthorizedException("JWT is invalid");
        } catch (UnsupportedJwtException ex) {
            log.warn("JWT is not supported", ex);
            throw new UnauthorizedException("JWT is not supported");
        }
    }
}
