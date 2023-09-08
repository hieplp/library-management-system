package dev.hieplp.library.service;

import dev.hieplp.library.common.entity.User;
import dev.hieplp.library.common.enums.user.UserStatus;
import dev.hieplp.library.common.exception.NotFoundException;
import dev.hieplp.library.common.helper.UserHelper;
import dev.hieplp.library.common.util.DateTimeUtil;
import dev.hieplp.library.config.AppConfig;
import dev.hieplp.library.config.security.CurrentUser;
import dev.hieplp.library.payload.request.user.CommonUserResponse;
import dev.hieplp.library.payload.request.user.profile.UpdateOwnProfileRequest;
import dev.hieplp.library.repository.UserRepository;
import dev.hieplp.library.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.BeanUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private final String NOT_FOUND_USER_ID = "NOT_FOUND_USER_ID";
    private final String FOUND_USER_ID = "FOUND_USER_ID";
    private final User ACTIVE_USER = User.builder()
            .userId(FOUND_USER_ID)
            .username("username")
            .email("email")
            .fullName("fullName")
            .avatar("avatar")
            .status(UserStatus.ACTIVE.getStatus())
            .build();

    @InjectMocks
    private UserServiceImpl userService;

    // Config
    @Mock
    private AppConfig appConfig;
    @Mock
    private CurrentUser currentUser;

    // Repository
    @Mock
    private UserRepository userRepo;

    // Helper
    @Mock
    private UserHelper userHelper;

    // Util
    @Mock
    private DateTimeUtil dateTimeUtil;


    @BeforeAll
    public static void setup() {
        MockitoAnnotations.openMocks(UserServiceTest.class);
    }

    @Nested
    class GetOwnProfileTest {
        @Test
        void shouldThrowNotFoundException_WhenUserNotFound() {
            doReturn(NOT_FOUND_USER_ID).when(currentUser).getUserId();
            doThrow(NotFoundException.class).when(userHelper).getActiveUser(NOT_FOUND_USER_ID);

            assertThrows(NotFoundException.class, () -> userService.getOwnProfile());
        }

        @Test
        void shouldSuccess_WhenAllConditionsAreMet() {
            var expected = new CommonUserResponse();
            BeanUtils.copyProperties(ACTIVE_USER, expected);

            doReturn(FOUND_USER_ID).when(currentUser).getUserId();
            doReturn(ACTIVE_USER).when(userHelper).getActiveUser(FOUND_USER_ID);

            var actual = userService.getOwnProfile();
            assertEquals(expected, actual);
        }
    }

    @Nested
    class UpdateOwnProfileTest {
        @Test
        void shouldThrowNotFoundException_WhenUserNotFound() {
            doReturn(NOT_FOUND_USER_ID).when(currentUser).getUserId();
            doThrow(NotFoundException.class).when(userHelper).getActiveUser(NOT_FOUND_USER_ID);

            assertThrows(NotFoundException.class, () -> userService.updateOwnProfile(new UpdateOwnProfileRequest()));
        }

        @Test
        void shouldThrowException_WhenSaveError() {
            doReturn(FOUND_USER_ID).when(currentUser).getUserId();
            doReturn(ACTIVE_USER).when(userHelper).getActiveUser(FOUND_USER_ID);
            doThrow(RuntimeException.class).when(userRepo).save(ACTIVE_USER);

            assertThrows(RuntimeException.class, () -> userService.updateOwnProfile(new UpdateOwnProfileRequest()));
        }

        @Test
        void shouldUpdateOnlyAvatar_WhenAvatarIsChanged() {
            var expected = new CommonUserResponse();
            BeanUtils.copyProperties(ACTIVE_USER, expected);
            expected.setAvatar("newAvatar");

            var request = new UpdateOwnProfileRequest();
            request.setAvatar("newAvatar");

            doReturn(FOUND_USER_ID).when(currentUser).getUserId();
            doReturn(ACTIVE_USER).when(userHelper).getActiveUser(FOUND_USER_ID);

            var actual = userService.updateOwnProfile(request);
            assertEquals(expected, actual);
        }

        @Test
        void shouldUpdateOnlyFullName_WhenFullNameIsChanged() {
            var expected = new CommonUserResponse();
            BeanUtils.copyProperties(ACTIVE_USER, expected);
            expected.setFullName("newFullName");

            var request = new UpdateOwnProfileRequest();
            request.setFullName("newFullName");

            doReturn(FOUND_USER_ID).when(currentUser).getUserId();
            doReturn(ACTIVE_USER).when(userHelper).getActiveUser(FOUND_USER_ID);

            var actual = userService.updateOwnProfile(request);
            assertEquals(expected, actual);
        }
    }
}
