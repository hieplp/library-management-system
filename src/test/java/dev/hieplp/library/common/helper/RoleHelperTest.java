package dev.hieplp.library.common.helper;

import dev.hieplp.library.common.entity.key.UserRoleKey;
import dev.hieplp.library.common.enums.user.UserRole;
import dev.hieplp.library.common.helper.impl.RoleHelperImpl;
import dev.hieplp.library.repository.UserRoleRepository;
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

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class RoleHelperTest {

    @InjectMocks
    private RoleHelperImpl roleHelper;

    @Mock
    private UserRoleRepository userRoleRepo;

    @BeforeAll
    public static void setup() {
        MockitoAnnotations.openMocks(RoleHelperTest.class);
    }

    @Nested
    class GetRolesByUserIdTest {
        private final String USER_ID = "userId";

        @Test
        void shouldThrowException_WhenGetRolesByUserIdIsError() {
            doThrow(RuntimeException.class).when(userRoleRepo).getRolesByUserId(USER_ID);
            assertThrows(RuntimeException.class, () -> roleHelper.getRolesByUserId(USER_ID));
        }

        @Test
        void shouldSuccess_WhenAllConditionsAreMet() {
            var roles = new ArrayList<dev.hieplp.library.common.entity.UserRole>();

            var userRole = new dev.hieplp.library.common.entity.UserRole();
            userRole.setId(UserRoleKey.builder()
                    .userId(USER_ID)
                    .role(UserRole.USER.getRole())
                    .build());
            roles.add(userRole);

            var expected = Set.of(UserRole.USER.getRole());

            doReturn(roles).when(userRoleRepo).getRolesByUserId(USER_ID);

            var actual = roleHelper.getRolesByUserId(USER_ID);
            assertEquals(expected, actual);
        }

    }
}
