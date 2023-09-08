package dev.hieplp.library.common.helper;

import dev.hieplp.library.common.entity.User;
import dev.hieplp.library.common.enums.user.UserStatus;
import dev.hieplp.library.common.exception.NotFoundException;
import dev.hieplp.library.common.exception.user.DuplicatedEmailException;
import dev.hieplp.library.common.exception.user.DuplicatedUsernameException;
import dev.hieplp.library.common.helper.impl.UserHelperImpl;
import dev.hieplp.library.repository.UserRepository;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class UserHelperTest {

    @InjectMocks
    private UserHelperImpl userHelper;

    @Mock
    private UserRepository userRepo;

    @BeforeAll
    public static void setup() {
        MockitoAnnotations.openMocks(UserHelperTest.class);
    }

    @Nested
    class ValidateUsernameTest {
        @Test
        void shouldThrowDuplicatedUsernameException_WhenUsernameIsDuplicated() {
            final var username = "username";
            doReturn(true).when(userRepo).existsByUsername(username);
            assertThrows(DuplicatedUsernameException.class, () -> userHelper.validateUsername(username));
        }

        @Test
        void shouldSuccess_WhenUsernameIsNotDuplicated() {
            final var username = "username";
            doReturn(false).when(userRepo).existsByUsername(username);
            userHelper.validateUsername(username);
        }
    }

    @Nested
    class ValidateEmailTest {
        @Test
        void shouldThrowDuplicatedEmailException_WhenEmailIsDuplicated() {
            final var email = "email";
            doReturn(true).when(userRepo).existsByEmail(email);
            assertThrows(DuplicatedEmailException.class, () -> userHelper.validateEmail(email));
        }

        @Test
        void shouldSuccess_WhenEmailIsNotDuplicated() {
            final var email = "email";
            doReturn(false).when(userRepo).existsByEmail(email);
            userHelper.validateEmail(email);
        }
    }

    @Nested
    class GetActiveUserTest {

        @Test
        void shouldThrowNotFoundException_WhenUserIsNotFound() {
            final var userId = "userId";
            doReturn(Optional.empty()).when(userRepo).findById(userId);
            assertThrows(NotFoundException.class, () -> userHelper.getActiveUser(userId));
        }

        @Test
        void shouldThrowNotFoundException_WhenUserIsNotActive() {
            var user = new User()
                    .setUserId("userId")
                    .setStatus(UserStatus.INACTIVE.getStatus());

            doReturn(Optional.of(user)).when(userRepo).findById(user.getUserId());

            assertThrows(NotFoundException.class, () -> userHelper.getActiveUser(user.getUserId()));
        }

        @Test
        void shouldSuccess_WhenUserIsActive() {
            var user = new User()
                    .setUserId("userId")
                    .setStatus(UserStatus.ACTIVE.getStatus());

            doReturn(Optional.of(user)).when(userRepo).findById(user.getUserId());

            assertEquals(user, userHelper.getActiveUser(user.getUserId()));
        }
    }
}
