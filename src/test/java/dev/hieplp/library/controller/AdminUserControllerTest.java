package dev.hieplp.library.controller;

import dev.hieplp.library.common.enums.response.ErrorCode;
import dev.hieplp.library.common.enums.response.SuccessCode;
import dev.hieplp.library.common.enums.user.UserStatus;
import dev.hieplp.library.common.payload.response.CommonResponse;
import dev.hieplp.library.controller.admin.AdminUserController;
import dev.hieplp.library.payload.request.user.CreateUserRequest;
import dev.hieplp.library.payload.request.user.UpdateUserRequest;
import dev.hieplp.library.payload.response.user.AdminUserResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminUserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AdminUserControllerTest extends ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminUserService userService;

    @Nested
    class CreateUserTest {
        private final String VALID_USERNAME = "username";
        private final String VALID_EMAIL = "email@hieplp.dev";

        private void callMvcTest(CreateUserRequest request, CommonResponse expected) throws Exception {
            final var api = post("/admin/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(request));

            mockMvc.perform(api)
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(content().json(toJson(expected)));
        }

        @Test
        void shouldReturnBadRequest_WhenUsernameIsBlank() throws Exception {
            var request = new CreateUserRequest();
            request.setUsername("");
            request.setEmail(VALID_EMAIL);

            var expected = new CommonResponse(ErrorCode.BAD_REQUEST);

            callMvcTest(request, expected);
        }

        @Test
        void shouldReturnBadRequest_WhenEmailIsBlank() throws Exception {
            var request = new CreateUserRequest();
            request.setUsername(VALID_USERNAME);
            request.setEmail("");

            var expected = new CommonResponse(ErrorCode.BAD_REQUEST);

            callMvcTest(request, expected);
        }

        @Test
        void shouldReturnBadRequest_WhenEmailIsInvalidFormat() throws Exception {
            var request = new CreateUserRequest();
            request.setUsername(VALID_USERNAME);
            request.setEmail("invalid email");

            var expected = new CommonResponse(ErrorCode.BAD_REQUEST);

            callMvcTest(request, expected);
        }

        @Test
        void shouldSuccess_WhenAllConditionsAreMet() throws Exception {
            var request = new CreateUserRequest();
            request.setUsername(VALID_USERNAME);
            request.setEmail(VALID_EMAIL);

            var response = AdminUserResponse.builder()
                    .createdAt(new Timestamp(0))
                    .createdBy("createdBy")
                    .modifiedAt(new Timestamp(0))
                    .modifiedBy("modifiedBy")
                    .userId("userId")
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .fullName(request.getFullName())
                    .avatar(request.getAvatar())
                    .status(UserStatus.ACTIVE.getStatus())
                    .build();

            var expected = new CommonResponse(SuccessCode.SUCCESS, response);

            doReturn(response).when(userService).createUser(request);

            callMvcTest(request, expected);
        }
    }

    @Nested
    class GetUserTest {
        private void callMvcTest(String userId, CommonResponse expected) throws Exception {
            final var api = get("/admin/users/" + userId);

            mockMvc.perform(api)
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(content().json(toJson(expected)));
        }

        @Test
        void shouldSuccess_WhenAllConditionAreMet() throws Exception {
            var userId = "userId";
            var response = AdminUserResponse.builder()
                    .createdAt(new Timestamp(0))
                    .createdBy(userId)
                    .modifiedAt(new Timestamp(0))
                    .modifiedBy(userId)
                    .userId(userId)
                    .username("username")
                    .email("email")
                    .fullName("fullName")
                    .avatar("avatar")
                    .status(UserStatus.ACTIVE.getStatus())
                    .build();
            var expected = new CommonResponse(SuccessCode.SUCCESS, response);

            doReturn(response).when(userService).getUser(userId);

            callMvcTest(userId, expected);
        }
    }

    @Nested
    class UpdateUserTest {
        private void callMvcTest(String userId, UpdateUserRequest request, CommonResponse expected) throws Exception {
            final var api = patch("/admin/users/" + userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(request));

            mockMvc.perform(api)
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(content().json(toJson(expected)));
        }

        @Test
        void shouldSuccess_WhenAllConditionAreMet() throws Exception {
            var userId = "userId";

            var request = new UpdateUserRequest();
            request.setFullName("fullName");
            request.setStatus(UserStatus.ACTIVE.getStatus());

            var response = AdminUserResponse.builder()
                    .createdAt(new Timestamp(0))
                    .createdBy(userId)
                    .modifiedAt(new Timestamp(0))
                    .modifiedBy(userId)
                    .userId(userId)
                    .username("username")
                    .email("email")
                    .fullName(request.getFullName())
                    .avatar("avatar")
                    .status(request.getStatus())
                    .build();
            var expected = new CommonResponse(SuccessCode.SUCCESS, response);

            doReturn(response).when(userService).updateUser(userId, request);

            callMvcTest(userId, request, expected);
        }
    }
}
