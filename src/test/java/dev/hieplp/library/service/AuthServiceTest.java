package dev.hieplp.library.service;

import dev.hieplp.library.common.config.OtpConfig;
import dev.hieplp.library.common.entity.Otp;
import dev.hieplp.library.common.entity.TempUser;
import dev.hieplp.library.common.enums.IdLength;
import dev.hieplp.library.common.enums.otp.OtpType;
import dev.hieplp.library.common.exception.NotFoundException;
import dev.hieplp.library.common.exception.otp.ExceededOtpQuotaException;
import dev.hieplp.library.common.exception.otp.ExpiredOtpException;
import dev.hieplp.library.common.exception.user.DuplicatedEmailException;
import dev.hieplp.library.common.exception.user.DuplicatedUsernameException;
import dev.hieplp.library.common.helper.OtpHelper;
import dev.hieplp.library.common.helper.UserHelper;
import dev.hieplp.library.common.util.*;
import dev.hieplp.library.payload.request.auth.register.ConfirmRegisterRequest;
import dev.hieplp.library.payload.request.auth.register.RequestToRegisterRequest;
import dev.hieplp.library.payload.request.auth.register.ResendRegisterOtpRequest;
import dev.hieplp.library.payload.response.auth.register.ConfirmRegisterResponse;
import dev.hieplp.library.payload.response.auth.register.RequestToRegisterResponse;
import dev.hieplp.library.payload.response.auth.register.ResendRegisterOtpResponse;
import dev.hieplp.library.repository.OtpRepository;
import dev.hieplp.library.repository.PasswordRepository;
import dev.hieplp.library.repository.TempUserRepository;
import dev.hieplp.library.repository.UserRepository;
import dev.hieplp.library.service.impl.AuthServiceImpl;
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

import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @InjectMocks
    private AuthServiceImpl authService;
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
    @Mock
    private MaskUtil maskUtil;
    @Mock
    private UserRepository userRepo;
    @Mock
    private OtpRepository otpRepo;
    @Mock
    private PasswordRepository passwordRepo;


    @BeforeAll
    public static void setup() {
        MockitoAnnotations.openMocks(AuthServiceTest.class);
    }

    @Nested
    class RequestToRegisterTest {
        private final OtpConfig otpConfig;

        RequestToRegisterTest() {
            otpConfig = new OtpConfig();
            otpConfig.setQuota(5);
            otpConfig.setResendQuota(3);
            otpConfig.setExpirationTime(300);
        }

        @Test
        void shouldThrowDuplicatedEmailException_WhenEmailIsDuplicated() {
            var request = new RequestToRegisterRequest();
            request.setEmail("old@hieplp.dev");
            doThrow(DuplicatedEmailException.class).when(userHelper).validateEmail(request.getEmail());
            assertThrows(DuplicatedEmailException.class, () -> authService.requestToRegister(request));
        }

        @Test
        void shouldThrowDuplicatedUsernameException_WhenUsernameIsDuplicated() {
            var request = new RequestToRegisterRequest();
            request.setUsername("hieplp_old");
            doThrow(DuplicatedUsernameException.class).when(userHelper).validateUsername(request.getUsername());
            assertThrows(DuplicatedUsernameException.class, () -> authService.requestToRegister(request));
        }

        @Test
        void shouldThrowExceededOtpQuotaException_WhenOtpQuotaIsExceeded() {
            var request = new RequestToRegisterRequest();
            request.setEmail("new@hieplp.dev");
            doThrow(ExceededOtpQuotaException.class).when(otpHelper).validateOtpQuota(request.getEmail(), OtpType.REGISTER);
            assertThrows(ExceededOtpQuotaException.class, () -> authService.requestToRegister(request));
        }

        @Test
        void shouldSuccess_WhenAllConditionsAreMet() {
            final var request = new RequestToRegisterRequest();
            request.setEmail("new@hieplp.dev");
            request.setUsername("hieplp_new");
            request.setPassword("password");

            final var otpId = "otpId";
            final var token = "token";
            final var currentTime = new Timestamp(System.currentTimeMillis());
            final var expiryTime = new Timestamp(currentTime.getTime() + otpConfig.getExpirationTime() * 1000L);
            final var expected = RequestToRegisterResponse.builder()
                    .otpId(otpId)
                    .maskedEmail(maskUtil.maskEmail(request.getEmail()))
                    .issuedTime(currentTime)
                    .expiryTime(expiryTime)
                    .build();

            doNothing().when(userHelper).validateEmail(request.getEmail());
            doNothing().when(userHelper).validateUsername(request.getUsername());
            doNothing().when(otpHelper).validateOtpQuota(request.getEmail(), OtpType.REGISTER);

            doReturn(otpConfig).when(otpHelper).getOtpConfig(OtpType.REGISTER);
            doReturn(otpId).when(generatorUtil).generateId(IdLength.OTP_ID);
            doReturn(token).when(generatorUtil).generateToken();
            doReturn(currentTime).when(dateTimeUtil).getCurrentTimestamp();
            doReturn(expiryTime).when(dateTimeUtil).addSeconds(currentTime, otpConfig.getExpirationTime());

            var result = authService.requestToRegister(request);
            assertEquals(expected.getOtpId(), result.getOtpId());
            assertEquals(expected.getMaskedEmail(), result.getMaskedEmail());
            assertEquals(expected.getExpiryTime(), result.getExpiryTime());
            assertEquals(expected.getIssuedTime(), result.getIssuedTime());
        }
    }

    @Nested
    class SendRegisterOtpTest {

        private final OtpConfig otpConfig;
        private final Timestamp currentTime;
        private final Timestamp expiryTime;
        private final int resendCount;
        private final String otpId;
        private final ResendRegisterOtpRequest request;
        private final Otp otp;

        SendRegisterOtpTest() {
            otpId = "otpId";

            otpConfig = new OtpConfig();
            otpConfig.setQuota(5);
            otpConfig.setResendQuota(3);
            otpConfig.setExpirationTime(300);

            currentTime = new Timestamp(System.currentTimeMillis());
            expiryTime = new Timestamp(currentTime.getTime() + otpConfig.getExpirationTime() * 1000L);
            resendCount = 0;

            request = new ResendRegisterOtpRequest();
            request.setOtpId(otpId);

            otp = new Otp();
            otp.setOtpId(otpId);
            otp.setIssueTime(currentTime);
            otp.setExpiryTime(expiryTime);
            otp.setResendCount(resendCount);
        }

        @Test
        void shouldThrowNotFoundException_WhenOtpIsNotFound() {
            doReturn(Optional.empty()).when(otpRepo).findById(otpId);
            assertThrows(NotFoundException.class, () -> authService.sendRegisterOtp(request));
        }

        @Test
        void shouldThrowExceededOtpQuotaException_WhenResendOtpQuotaIsExceeded() {
            doReturn(Optional.of(otp)).when(otpRepo).findById(otpId);
            doReturn(otpConfig).when(otpHelper).getOtpConfig(OtpType.REGISTER);
            doThrow(ExceededOtpQuotaException.class).when(otpHelper).validateResendOtpQuota(otp, otpConfig);

            assertThrows(ExceededOtpQuotaException.class, () -> authService.sendRegisterOtp(request));
        }

        @Test
        void shouldSuccess_WhenAllConditionsAreMet() {
            final var expected = ResendRegisterOtpResponse.builder()
                    .otpId(otpId)
                    .maskedEmail(maskUtil.maskEmail(otp.getSendTo()))
                    .issuedTime(currentTime)
                    .expiryTime(expiryTime)
                    .resendCount(resendCount + 1)
                    .resendQuota(otpConfig.getResendQuota())
                    .build();

            doReturn(Optional.of(otp)).when(otpRepo).findById(otpId);
            doReturn(otpConfig).when(otpHelper).getOtpConfig(OtpType.REGISTER);
            doReturn(currentTime).when(dateTimeUtil).getCurrentTimestamp();
            doReturn(expiryTime).when(dateTimeUtil).addSeconds(currentTime, otpConfig.getExpirationTime());

            var result = authService.sendRegisterOtp(request);
            assertEquals(expected.getOtpId(), result.getOtpId());
            assertEquals(expected.getMaskedEmail(), result.getMaskedEmail());
            assertEquals(expected.getExpiryTime().getTime(), result.getExpiryTime().getTime());
            assertEquals(expected.getIssuedTime(), result.getIssuedTime());
            assertEquals(expected.getResendCount(), result.getResendCount());
            assertEquals(expected.getResendQuota(), result.getResendQuota());
        }
    }

    @Nested
    class ConfirmRegister {
        final Otp otp;
        final ConfirmRegisterRequest request;
        private final String otpId;
        private final String token;

        ConfirmRegister() {
            otpId = "otpId";
            token = "token";

            otp = new Otp();
            otp.setOtpId(otpId);
            otp.setToken(token);

            request = new ConfirmRegisterRequest();
            request.setToken(token);
        }

        @Test
        void shouldThrowNotFoundException_WhenOtpIsNotFound() {
            doReturn(Optional.empty()).when(otpRepo).findByTokenAndType(token, OtpType.REGISTER.getType());
            assertThrows(NotFoundException.class, () -> authService.confirmRegister(request));
        }

        @Test
        void shouldThrowExpiredOtpException_WhenOtpIsExpired() {
            doReturn(Optional.of(otp)).when(otpRepo).findByTokenAndType(token, OtpType.REGISTER.getType());
            doThrow(ExpiredOtpException.class).when(otpHelper).validateOtpLifeTime(otp);

            assertThrows(ExpiredOtpException.class, () -> authService.confirmRegister(request));
        }

        @Test
        void shouldThrowNotFoundException_WhenTempUserIsNotFound() {
            doReturn(Optional.of(otp)).when(otpRepo).findByTokenAndType(token, OtpType.REGISTER.getType());
            doNothing().when(otpHelper).validateOtpLifeTime(otp);
            doReturn(Optional.empty()).when(tempUserRepo).findByOtp(otp);

            assertThrows(NotFoundException.class, () -> authService.confirmRegister(request));
        }

        @Test
        void shouldThrowDuplicatedEmailException_WhenEmailIsDuplicated() {
            final var tempUser = new TempUser();
            tempUser.setUserId("userId");
            tempUser.setEmail("email");
            tempUser.setOtp(otp);

            doReturn(Optional.of(otp)).when(otpRepo).findByTokenAndType(token, OtpType.REGISTER.getType());
            doNothing().when(otpHelper).validateOtpLifeTime(otp);
            doReturn(Optional.of(tempUser)).when(tempUserRepo).findByOtp(otp);
            doThrow(DuplicatedEmailException.class).when(userHelper).validateEmail(tempUser.getEmail());

            assertThrows(DuplicatedEmailException.class, () -> authService.confirmRegister(request));
        }

        @Test
        void shouldThrowDuplicatedUsernameException_WhenUsernameIsDuplicated() {
            final var tempUser = new TempUser();
            tempUser.setUserId("userId");
            tempUser.setEmail("email");
            tempUser.setOtp(otp);

            doReturn(Optional.of(otp)).when(otpRepo).findByTokenAndType(token, OtpType.REGISTER.getType());
            doNothing().when(otpHelper).validateOtpLifeTime(otp);
            doReturn(Optional.of(tempUser)).when(tempUserRepo).findByOtp(otp);
            doNothing().when(userHelper).validateEmail(tempUser.getEmail());
            doThrow(DuplicatedUsernameException.class).when(userHelper).validateUsername(tempUser.getUsername());

            assertThrows(DuplicatedUsernameException.class, () -> authService.confirmRegister(request));
        }

        @Test
        void shouldSuccess_WhenAllConditionsAreMet() {
            final var tempUser = new TempUser();
            tempUser.setUserId("userId");
            tempUser.setEmail("email");
            tempUser.setOtp(otp);

            var expected = ConfirmRegisterResponse.builder()
                    .maskedEmail(maskUtil.maskEmail(tempUser.getEmail()))
                    .build();

            doReturn(Optional.of(otp)).when(otpRepo).findByTokenAndType(token, OtpType.REGISTER.getType());
            doNothing().when(otpHelper).validateOtpLifeTime(otp);
            doReturn(Optional.of(tempUser)).when(tempUserRepo).findByOtp(otp);
            doNothing().when(userHelper).validateEmail(tempUser.getEmail());
            doNothing().when(userHelper).validateUsername(tempUser.getUsername());
            doNothing().when(tempUserRepo).delete(tempUser);
            doNothing().when(otpRepo).delete(otp);

            var result = authService.confirmRegister(request);
            assertEquals(expected.getMaskedEmail(), result.getMaskedEmail());
        }

    }
}
