package dev.hieplp.library.controller;

import dev.hieplp.library.common.enums.response.ErrorCode;
import dev.hieplp.library.common.enums.response.SuccessCode;
import dev.hieplp.library.common.model.TokenModel;
import dev.hieplp.library.common.payload.response.CommonResponse;
import dev.hieplp.library.payload.request.auth.LoginRequest;
import dev.hieplp.library.payload.request.auth.register.ConfirmRegisterRequest;
import dev.hieplp.library.payload.request.auth.register.RequestToRegisterRequest;
import dev.hieplp.library.payload.request.auth.register.ResendRegisterOtpRequest;
import dev.hieplp.library.payload.request.auth.verify.ConfirmVerifyRequest;
import dev.hieplp.library.payload.request.auth.verify.RequestToVerifyRequest;
import dev.hieplp.library.payload.request.auth.verify.ResendVerifyOtpRequest;
import dev.hieplp.library.payload.response.auth.LoginResponse;
import dev.hieplp.library.payload.response.auth.register.ConfirmRegisterResponse;
import dev.hieplp.library.payload.response.auth.register.RequestToRegisterResponse;
import dev.hieplp.library.payload.response.auth.register.ResendRegisterOtpResponse;
import dev.hieplp.library.payload.response.auth.verify.ConfirmVerifyResponse;
import dev.hieplp.library.payload.response.auth.verify.RequestToVerifyResponse;
import dev.hieplp.library.payload.response.auth.verify.ResendVerifyOtpResponse;
import dev.hieplp.library.service.AuthService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest extends ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Nested
    class RequestToRegisterTest {
        private final String VALID_USERNAME = "username";
        private final String VALID_EMAIL = "valid@hieplp.dev";
        private final String VALID_PASSWORD = "password";

        private void callMvcTest(RequestToRegisterRequest request, CommonResponse expected) throws Exception {
            final var api = post("/auth/request-to-register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(request));

            mockMvc.perform(api)
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(content().json(toJson(expected)));
        }

        @Test
        void shouldReturnBadRequest_WhenUsernameIsBlank() throws Exception {
            var request = new RequestToRegisterRequest();
            request.setUsername("");
            request.setEmail(VALID_EMAIL);
            request.setPassword(VALID_PASSWORD);

            var expected = new CommonResponse(ErrorCode.BAD_REQUEST);

            callMvcTest(request, expected);
        }

        @Test
        void shouldReturnBadRequest_WhenEmailIsBlank() throws Exception {
            var request = new RequestToRegisterRequest();
            request.setUsername(VALID_USERNAME);
            request.setPassword(VALID_PASSWORD);
            request.setEmail("");

            var expected = new CommonResponse(ErrorCode.BAD_REQUEST);

            callMvcTest(request, expected);
        }

        @Test
        void shouldReturnBadRequest_WhenEmailIsNotBlankButInvalidFormat() throws Exception {
            var request = new RequestToRegisterRequest();
            request.setUsername(VALID_USERNAME);
            request.setPassword(VALID_PASSWORD);
            request.setEmail("invalid");

            var expected = new CommonResponse(ErrorCode.BAD_REQUEST);

            callMvcTest(request, expected);
        }

        @Test
        void shouldReturnBadRequest_WhenPasswordIsBlank() throws Exception {
            var request = new RequestToRegisterRequest();
            request.setUsername(VALID_USERNAME);
            request.setEmail(VALID_EMAIL);
            request.setPassword("");

            var expected = new CommonResponse(ErrorCode.BAD_REQUEST);

            callMvcTest(request, expected);
        }

        @Test
        void shouldSuccess_WhenAllConditionsAreMet() throws Exception {
            final var request = new RequestToRegisterRequest();
            request.setUsername(VALID_USERNAME);
            request.setEmail(VALID_EMAIL);
            request.setPassword(VALID_PASSWORD);

            var response = RequestToRegisterResponse.builder()
                    .otpId("otpId")
                    .maskedEmail("maskedEmail")
                    .expiryTime(new Timestamp(System.currentTimeMillis()))
                    .issuedTime(new Timestamp(System.currentTimeMillis()))
                    .build();
            var expected = new CommonResponse(SuccessCode.SUCCESS, response);

            doReturn(response).when(authService).requestToRegister(request);

            callMvcTest(request, expected);
        }
    }

    @Nested
    class ResendRegisterOtpTest {
        private void callMvcTest(ResendRegisterOtpRequest request, CommonResponse expected) throws Exception {
            final var api = post("/auth/resend-register-otp")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(request));

            mockMvc.perform(api)
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(content().json(toJson(expected)));
        }

        @Test
        void shouldReturnBadRequest_WhenOtpIdIsBlank() throws Exception {
            var request = new ResendRegisterOtpRequest();
            request.setOtpId("");

            var expected = new CommonResponse(ErrorCode.BAD_REQUEST);

            callMvcTest(request, expected);
        }

        @Test
        void shouldSuccess_WhenAllConditionsAreMet() throws Exception {
            var request = new ResendRegisterOtpRequest();
            request.setOtpId("validOtpId");

            var response = ResendRegisterOtpResponse.builder()
                    .otpId("otpId")
                    .maskedEmail("maskedEmail")
                    .expiryTime(new Timestamp(System.currentTimeMillis()))
                    .issuedTime(new Timestamp(System.currentTimeMillis()))
                    .build();
            var expected = new CommonResponse(SuccessCode.SUCCESS, response);

            doReturn(response).when(authService).resendRegisterOtp(request);

            callMvcTest(request, expected);
        }
    }

    @Nested
    class ConfirmRegisterTest {
        private void callMvcTest(ConfirmRegisterRequest request, CommonResponse expected) throws Exception {
            final var api = post("/auth/confirm-register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(request));

            mockMvc.perform(api)
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(content().json(toJson(expected)));
        }

        @Test
        void shouldReturnBadRequest_WhenTokenIsBlank() throws Exception {
            final var request = new ConfirmRegisterRequest();
            request.setToken("");

            final var expected = new CommonResponse(ErrorCode.BAD_REQUEST);

            callMvcTest(request, expected);
        }

        @Test
        void shouldSuccess_WhenAllConditionsAreMet() throws Exception {
            final var request = new ConfirmRegisterRequest();
            request.setToken("validToken");

            final var response = ConfirmRegisterResponse.builder()
                    .maskedEmail("maskedEmail")
                    .build();
            final var expected = new CommonResponse(SuccessCode.SUCCESS, response);

            doReturn(response).when(authService).confirmRegister(request);

            callMvcTest(request, expected);
        }
    }

    @Nested
    class LoginTest {
        private final String VALID_USERNAME = "username";
        private final String VALID_PASSWORD = "password";

        private void callMvcTest(LoginRequest request, CommonResponse expected) throws Exception {
            final var api = post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(request));

            mockMvc.perform(api)
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(content().json(toJson(expected)));
        }

        @Test
        void shouldReturnBadRequest_WhenUsernameIsBlank() throws Exception {
            var request = new LoginRequest();
            request.setUsername("");
            request.setPassword(VALID_PASSWORD);

            var expected = new CommonResponse(ErrorCode.BAD_REQUEST);

            callMvcTest(request, expected);
        }

        @Test
        void shouldReturnBadRequest_WhenPasswordIsBlank() throws Exception {
            var request = new LoginRequest();
            request.setUsername(VALID_USERNAME);
            request.setPassword("");

            var expected = new CommonResponse(ErrorCode.BAD_REQUEST);

            callMvcTest(request, expected);
        }

        @Test
        void shouldSuccess_WhenAllConditionsAreMet() throws Exception {
            var request = new LoginRequest();
            request.setUsername(VALID_USERNAME);
            request.setPassword(VALID_PASSWORD);

            var response = LoginResponse.builder()
                    .accessToken(TokenModel.builder()
                            .token("accessToken")
                            .expiredAt(new Timestamp(System.currentTimeMillis()))
                            .build())
                    .refreshToken(TokenModel.builder()
                            .token("refreshToken")
                            .expiredAt(new Timestamp(System.currentTimeMillis()))
                            .build())
                    .build();
            var expected = new CommonResponse(SuccessCode.SUCCESS, response);

            doReturn(response).when(authService).login(request);

            callMvcTest(request, expected);
        }
    }

    @Nested
    class RefreshTokenTest {
        private void callMvcTest(CommonResponse expected) throws Exception {
            final var api = post("/auth/refresh-access-token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(EMPTY_CONTENT);

            mockMvc.perform(api)
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(content().json(toJson(expected)));
        }

        @Test
        void shouldSuccess_WhenAllConditionsAreMet() throws Exception {
            var response = TokenModel.builder()
                    .token("accessToken")
                    .expiredAt(new Timestamp(System.currentTimeMillis()))
                    .build();
            var expected = new CommonResponse(SuccessCode.SUCCESS, response);

            doReturn(response).when(authService).refreshAccessToken(any());

            callMvcTest(expected);
        }
    }

    @Nested
    class RequestToVerifyTest {

        private final String VALID_USERNAME = "username";
        private final String VALID_EMAIL = "valid@hieplp.dev";

        private void callMvcTest(RequestToVerifyRequest request, CommonResponse expected) throws Exception {
            final var api = post("/auth/request-to-verify")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(request));

            mockMvc.perform(api)
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(content().json(toJson(expected)));
        }

        @Test
        void shouldReturnBadRequest_WhenUsernameIsBlank() throws Exception {
            var request = new RequestToVerifyRequest();
            request.setUsername("");
            request.setEmail(VALID_EMAIL);

            var expected = new CommonResponse(ErrorCode.BAD_REQUEST);

            callMvcTest(request, expected);
        }

        @Test
        void shouldReturnBadRequest_WhenEmailIsBlank() throws Exception {
            var request = new RequestToVerifyRequest();
            request.setUsername(VALID_USERNAME);
            request.setEmail("");

            var expected = new CommonResponse(ErrorCode.BAD_REQUEST);

            callMvcTest(request, expected);
        }

        @Test
        void shouldReturnBadRequest_WhenEmailIsNotBlankButInvalidFormat() throws Exception {
            var request = new RequestToVerifyRequest();
            request.setUsername(VALID_USERNAME);
            request.setEmail("invalid");

            var expected = new CommonResponse(ErrorCode.BAD_REQUEST);

            callMvcTest(request, expected);
        }

        @Test
        void shouldReturnSuccess_WhenAllConditionsAreMet() throws Exception {
            var request = new RequestToVerifyRequest();
            request.setUsername(VALID_USERNAME);
            request.setEmail(VALID_EMAIL);

            var response = RequestToVerifyResponse.builder()
                    .otpId("otpId")
                    .maskedEmail("maskedEmail")
                    .expiryTime(new Timestamp(System.currentTimeMillis()))
                    .issuedTime(new Timestamp(System.currentTimeMillis()))
                    .build();
            var expected = new CommonResponse(SuccessCode.SUCCESS, response);

            doReturn(response).when(authService).requestToVerify(request);

            callMvcTest(request, expected);
        }
    }

    @Nested
    class ResendVerifyOtpTest {
        private void callMvcTest(ResendVerifyOtpRequest request, CommonResponse expected) throws Exception {
            final var api = post("/auth/resend-verify-otp")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(request));

            mockMvc.perform(api)
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(content().json(toJson(expected)));
        }

        @Test
        void shouldReturnBadRequest_WhenOtpIdIsBlank() throws Exception {
            var request = new ResendVerifyOtpRequest();
            request.setOtpId("");

            var expected = new CommonResponse(ErrorCode.BAD_REQUEST);

            callMvcTest(request, expected);
        }

        @Test
        void shouldSuccess_WhenAllConditionsAreMet() throws Exception {
            var request = new ResendVerifyOtpRequest();
            request.setOtpId("validOtpId");

            var response = ResendVerifyOtpResponse.builder()
                    .otpId("otpId")
                    .maskedEmail("maskedEmail")
                    .expiryTime(new Timestamp(System.currentTimeMillis()))
                    .issuedTime(new Timestamp(System.currentTimeMillis()))
                    .build();
            var expected = new CommonResponse(SuccessCode.SUCCESS, response);

            doReturn(response).when(authService).resendVerifyOtp(request);

            callMvcTest(request, expected);
        }
    }

    @Nested
    class ConfirmVerifyTest {

        private final String VALID_TOKEN = "token";
        private final String VALID_PASSWORD = "password";

        private void callMvcTest(ConfirmVerifyRequest request, CommonResponse expected) throws Exception {
            final var api = post("/auth/confirm-verify")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(request));

            mockMvc.perform(api)
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(content().json(toJson(expected)));

        }


        @Test
        void shouldReturnBadRequest_WhenTokenIsBlank() throws Exception {
            var request = new ConfirmVerifyRequest();
            request.setToken("");
            request.setPassword(VALID_PASSWORD);

            var expected = new CommonResponse(ErrorCode.BAD_REQUEST);

            callMvcTest(request, expected);
        }

        @Test
        void shouldReturnBadRequest_WhenPasswordIsBlank() throws Exception {
            var request = new ConfirmVerifyRequest();
            request.setToken(VALID_TOKEN);
            request.setPassword("");

            var expected = new CommonResponse(ErrorCode.BAD_REQUEST);

            callMvcTest(request, expected);
        }

        @Test
        void shouldSuccess_WhenAllConditionsAreMet() throws Exception {
            var request = new ConfirmVerifyRequest();
            request.setToken(VALID_TOKEN);
            request.setPassword(VALID_PASSWORD);

            var response = ConfirmVerifyResponse.builder()
                    .maskedEmail("maskedEmail")
                    .build();
            var expected = new CommonResponse(SuccessCode.SUCCESS, response);

            doReturn(response).when(authService).confirmVerify(request);

            callMvcTest(request, expected);
        }
    }
}
