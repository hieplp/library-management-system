package dev.hieplp.library.controller;


import dev.hieplp.library.common.enums.response.SuccessCode;
import dev.hieplp.library.common.enums.user.UserStatus;
import dev.hieplp.library.common.payload.response.CommonResponse;
import dev.hieplp.library.payload.request.user.profile.UpdateOwnProfileRequest;
import dev.hieplp.library.payload.response.user.CommonUserResponse;
import dev.hieplp.library.service.UserService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest extends ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Nested
    class GetOwnProfile {
        private void callMvcTest(CommonResponse expected) throws Exception {
            final var api = get("/users/profile");

            mockMvc.perform(api)
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(content().json(toJson(expected)));
        }

        @Test
        void shouldSuccess_WhenAllConditionsAreMet() throws Exception {
            var response = CommonUserResponse.builder()
                    .userId("userId")
                    .username("username")
                    .email("email")
                    .fullName("fullName")
                    .avatar("avatar")
                    .status(UserStatus.ACTIVE.getStatus())
                    .build();
            var expected = new CommonResponse(SuccessCode.SUCCESS, response);

            doReturn(response).when(userService).getOwnProfile();

            callMvcTest(expected);
        }
    }

    @Nested
    class UpdateOwnProfileTest {
        private void callMvcTest(UpdateOwnProfileRequest request, CommonResponse expected) throws Exception {
            final var api = patch("/users/profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(request));

            mockMvc.perform(api)
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(content().json(toJson(expected)));
        }

        @Test
        void shouldSuccess_WhenAllConditionsAreMet() throws Exception {
            var request = new UpdateOwnProfileRequest();
            request.setAvatar("avatar");
            request.setFullName("fullName");

            var response = CommonUserResponse.builder()
                    .userId("userId")
                    .username("username")
                    .email("email")
                    .fullName(request.getFullName())
                    .avatar(request.getAvatar())
                    .status(UserStatus.ACTIVE.getStatus())
                    .build();
            var expected = new CommonResponse(SuccessCode.SUCCESS, response);


            doReturn(response).when(userService).updateOwnProfile(request);

            callMvcTest(request, expected);
        }
    }
}
