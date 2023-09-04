package dev.hieplp.library.service;

import dev.hieplp.library.common.helper.OtpHelper;
import dev.hieplp.library.common.helper.UserHelper;
import dev.hieplp.library.common.util.DateTimeUtil;
import dev.hieplp.library.common.util.EmailUtil;
import dev.hieplp.library.common.util.EncryptUtil;
import dev.hieplp.library.common.util.GeneratorUtil;
import dev.hieplp.library.repository.TempUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @InjectMocks
    private AuthService authService;
    @Mock
    private UserHelper userHelper;
    @Mock
    private OtpHelper otpHelper;
    @Mock
    private GeneratorUtil generatorUtil;
    @Mock
    private DateTimeUtil dateTimeUtil;
    @Mock
    private EncryptUtil encryptUtil;
    @Mock
    private TempUserRepository tempUserRepo;
    @Mock
    private EmailUtil emailUtil;

    @Test
    public void test() {

    }
}
