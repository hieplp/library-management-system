package dev.hieplp.library.config.filter;

import dev.hieplp.library.common.enums.token.TokenHeader;
import dev.hieplp.library.common.exception.UnauthorizedException;
import dev.hieplp.library.common.util.TokenUtil;
import dev.hieplp.library.config.security.CurrentUser;
import dev.hieplp.library.config.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Set;

@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private PrivateKey tokenPrivateKey;
    @Autowired
    private CurrentUser currentUser;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            log.debug("Refresh token authentication filter");
            var token = getAccessToken(request);
            var claims = tokenUtil.parseToken(token, tokenPrivateKey);
            setAuthenticationContext(token, claims, request);
        } catch (Exception e) {
            log.debug("Error while processing refresh token authentication filter", e);
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    private String getAccessToken(HttpServletRequest request) {
        var header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            log.warn("Authorization header is null or not start with Bearer");
            throw new UnauthorizedException("Authorization header is null or not start with Bearer");
        }

        return header.split(" ")[1].trim();
    }

    private void setAuthenticationContext(String token, Jws<Claims> claims, HttpServletRequest request) {
        // Get user details from token
        var userDetails = getUserDetails(token, claims);

        // Update current user
        setCurrentUser(userDetails);

        var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, null);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private UserPrincipal getUserDetails(String token, Jws<Claims> claims) {
        var userDetails = new UserPrincipal();

        var header = claims.getHeader();

        // Copy roles from token to user details
        var roles = header.get(TokenHeader.ROLES.getHeader());
        @SuppressWarnings("unchecked")
        var roleSet = Set.copyOf((ArrayList<Byte>) roles);
        userDetails.setRoles(roleSet);

        // Copy userId from token to user details
        var userId = header.get(TokenHeader.USER_ID.getHeader());
        userDetails.setUserId(String.valueOf(userId));

        // Copy token info from token to user details
        var tokenType = header.get(TokenHeader.TOKEN_TYPE.getHeader());
        userDetails.setTokenType(Byte.valueOf(String.valueOf(tokenType)));

        //
        userDetails.setToken(token);

        return userDetails;
    }

    private void setCurrentUser(UserPrincipal userPrincipal) {
        currentUser
                .setUserId(userPrincipal.getUserId())
                .setToken(userPrincipal.getToken())
                .setTokenType(userPrincipal.getTokenType());
        log.info("Set current user: {}", currentUser);
    }
}
