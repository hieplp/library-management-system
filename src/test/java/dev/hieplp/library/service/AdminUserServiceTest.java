package dev.hieplp.library.service;

import dev.hieplp.library.common.entity.User;
import dev.hieplp.library.common.enums.user.Role;
import dev.hieplp.library.common.enums.user.UserStatus;
import dev.hieplp.library.common.exception.NotFoundException;
import dev.hieplp.library.common.exception.user.DuplicatedEmailException;
import dev.hieplp.library.common.exception.user.DuplicatedUsernameException;
import dev.hieplp.library.common.helper.UserHelper;
import dev.hieplp.library.common.util.DateTimeUtil;
import dev.hieplp.library.common.util.GeneratorUtil;
import dev.hieplp.library.config.security.CurrentUser;
import dev.hieplp.library.payload.request.user.CreateUserRequest;
import dev.hieplp.library.payload.request.user.UpdateUserRequest;
import dev.hieplp.library.payload.response.user.AdminUserResponse;
import dev.hieplp.library.repository.PasswordRepository;
import dev.hieplp.library.repository.UserRepository;
import dev.hieplp.library.service.impl.AdminUserServiceImpl;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class AdminUserServiceTest {
    @InjectMocks
    private AdminUserServiceImpl adminUserService;

    @Mock
    private CurrentUser currentUser;

    @Mock
    private UserHelper userHelper;

    @Mock
    private UserRepository userRepo;
    @Mock
    private PasswordRepository passwordRepo;

    @Mock
    private GeneratorUtil generatorUtil;
    @Mock
    private DateTimeUtil dateTimeUtil;

    @BeforeAll
    public static void setup() {
        MockitoAnnotations.openMocks(AdminUserServiceTest.class);
    }

    @Nested
    class GetUserTest {
        @Test
        void shouldSuccess_WhenAllConditionsAreMet() {
            var user = new User()
                    .setUserId("userId")
                    .setFullName("fullName")
                    .setUsername("username")
                    .setEmail("email")
                    .setStatus(UserStatus.ACTIVE.getStatus());

            var expected = new AdminUserResponse();
            BeanUtils.copyProperties(user, expected);

            doReturn(user).when(userHelper).getUser(user.getUserId());

            assertEquals(expected, adminUserService.getUser(user.getUserId()));
        }
    }

    @Nested
    class CreateUserTest {
        private final String USERNAME = "username";
        private final String EMAIL = "email";
        private final String USER_ID = "userId";
        private final String FULL_NAME = "fullName";
        private final String AVATAR = "avatar";

        private final CreateUserRequest request;


        public CreateUserTest() {
            request = new CreateUserRequest();
            request.setUsername(USERNAME);
            request.setEmail(EMAIL);
            request.setFullName(FULL_NAME);
            request.setAvatar(AVATAR);
        }

        @Test
        void shouldThrowDuplicatedUsernameException_WhenUsernameIsDuplicated() {
            doThrow(DuplicatedUsernameException.class).when(userHelper).validateUsername(request.getUsername());

            assertThrows(DuplicatedUsernameException.class, () -> adminUserService.createUser(request));
        }

        @Test
        void shouldThrowDuplicatedEmailException_WhenEmailIsDuplicated() {
            doNothing().when(userHelper).validateUsername(request.getUsername());
            doThrow(DuplicatedEmailException.class).when(userHelper).validateEmail(request.getEmail());

            assertThrows(DuplicatedEmailException.class, () -> adminUserService.createUser(request));
        }

        @Test
        void shouldSuccess_WhenRolesAreNull() {
            doNothing().when(userHelper).validateUsername(request.getUsername());
            doNothing().when(userHelper).validateEmail(request.getEmail());

            doReturn(USER_ID).when(generatorUtil).generateId(any());
            doReturn(new Timestamp(0)).when(dateTimeUtil).getCurrentTimestamp();
            doReturn(USER_ID).when(currentUser).getUserId();

            assert adminUserService.createUser(request) != null;
        }

        @Test
        void shouldSuccess_WhenRolesAreEmpty() {
            request.setRoles(new ArrayList<>());

            doNothing().when(userHelper).validateUsername(request.getUsername());
            doNothing().when(userHelper).validateEmail(request.getEmail());

            doReturn("userId").when(generatorUtil).generateId(any());
            doReturn(new Timestamp(0)).when(dateTimeUtil).getCurrentTimestamp();
            doReturn("userId").when(currentUser).getUserId();

            assert adminUserService.createUser(request) != null;
        }

        @Test
        void shouldSuccess_WhenRolesAreNotEmpty() {
            final var INVALID_ROLE = (byte) 999;
            request.setRoles(List.of(Role.ADMIN.getRole(), INVALID_ROLE));

            doNothing().when(userHelper).validateUsername(request.getUsername());
            doNothing().when(userHelper).validateEmail(request.getEmail());
            doReturn(USER_ID).when(generatorUtil).generateId(any());
            doReturn(new Timestamp(0)).when(dateTimeUtil).getCurrentTimestamp();
            doReturn(USER_ID).when(currentUser).getUserId();

            assert adminUserService.createUser(request) != null;
        }

        @Test
        void shouldThrowException_WhenSavePasswordError() {
            doNothing().when(userHelper).validateUsername(request.getUsername());
            doNothing().when(userHelper).validateEmail(request.getEmail());

            doReturn(USER_ID).when(generatorUtil).generateId(any());
            doReturn(new Timestamp(0)).when(dateTimeUtil).getCurrentTimestamp();
            doReturn(USER_ID).when(currentUser).getUserId();

            doThrow(RuntimeException.class).when(passwordRepo).save(any());

            assertThrows(RuntimeException.class, () -> adminUserService.createUser(request));
        }

        @Test
        void shouldSuccess_WhenAllConditionsAreMet() {
            final var currentTime = new Timestamp(0);

            final var user = new User()
                    .setUserId(USER_ID)
                    .setUsername(USERNAME)
                    .setEmail(EMAIL)
                    .setFullName(FULL_NAME)
                    .setAvatar(AVATAR)
                    .setStatus(UserStatus.NOT_VERIFIED.getStatus())
                    .setCreatedBy(USER_ID)
                    .setCreatedAt(currentTime)
                    .setModifiedBy(USER_ID)
                    .setModifiedAt(currentTime);

            final var expected = new AdminUserResponse();
            BeanUtils.copyProperties(user, expected);

            doNothing().when(userHelper).validateUsername(request.getUsername());
            doNothing().when(userHelper).validateEmail(request.getEmail());

            doReturn(USER_ID).when(generatorUtil).generateId(any());
            doReturn(currentTime).when(dateTimeUtil).getCurrentTimestamp();
            doReturn(USER_ID).when(currentUser).getUserId();

            var actual = adminUserService.createUser(request);
            assertEquals(expected, actual);
        }
    }

    @Nested
    class UpdateUserTest {
        private final String USER_ID = "userId";
        private final String FULL_NAME = "fullName";
        private final String AVATAR = "avatar";

        private final UpdateUserRequest request;
        private final User user;

        public UpdateUserTest() {
            request = new UpdateUserRequest();

            user = new User()
                    .setUserId(USER_ID)
                    .setFullName(FULL_NAME)
                    .setAvatar(AVATAR);
        }

        @Test
        void shouldThrowNotFoundException_WhenUserIsNotFound() {
            doThrow(NotFoundException.class).when(userHelper).getUser(USER_ID);

            assertThrows(NotFoundException.class, () -> adminUserService.updateUser(USER_ID, request));
        }

        @Test
        void shouldSuccess_WhenFullNameIsNull() {
            request.setFullName(null);

            doReturn(user).when(userHelper).getUser(USER_ID);

            assert adminUserService.updateUser(USER_ID, request) != null;
        }

        @Test
        void shouldSuccess_WhenStatusIsNull() {
            request.setFullName(FULL_NAME);
            request.setStatus(null);

            doReturn(user).when(userHelper).getUser(USER_ID);

            assert adminUserService.updateUser(USER_ID, request) != null;
        }

        @Test
        void shouldThrowException_WhenSaveUserError() {
            request.setFullName(FULL_NAME);
            request.setStatus(UserStatus.ACTIVE.getStatus());

            doReturn(user).when(userHelper).getUser(USER_ID);
            doThrow(RuntimeException.class).when(userRepo).save(any());

            assertThrows(RuntimeException.class, () -> adminUserService.updateUser(USER_ID, request));
        }

        @Test
        void shouldSuccess_WhenAllConditionsAreMet() {

            request.setFullName(FULL_NAME);
            request.setStatus(UserStatus.ACTIVE.getStatus());

            final var currentTime = new Timestamp(0);

            user
                    .setStatus(request.getStatus())
                    .setModifiedBy(USER_ID)
                    .setModifiedAt(currentTime);

            final var expected = new AdminUserResponse();
            BeanUtils.copyProperties(user, expected);

            doReturn(USER_ID).when(currentUser).getUserId();
            doReturn(user).when(userHelper).getUser(USER_ID);
            doReturn(currentTime).when(dateTimeUtil).getCurrentTimestamp();

            var actual = adminUserService.updateUser(USER_ID, request);
            assertEquals(expected, actual);
        }

    }
}
