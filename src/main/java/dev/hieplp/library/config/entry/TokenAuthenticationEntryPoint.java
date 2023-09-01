package dev.hieplp.library.config.entry;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.hieplp.library.common.enums.response.ErrorCode;
import dev.hieplp.library.common.payload.response.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class TokenAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException {
        log.warn("Unauthorized request: {} with message: {}", httpServletRequest.getRequestURI(), e.getMessage());

        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        var responseStream = httpServletResponse.getOutputStream();

        var mapper = new ObjectMapper();
        var data = new CommonResponse(ErrorCode.UNAUTHORIZED, e.getMessage());
        mapper.writeValue(responseStream, data);

        responseStream.flush();
    }
}