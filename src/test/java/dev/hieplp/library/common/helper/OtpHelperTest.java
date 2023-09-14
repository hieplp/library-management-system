package dev.hieplp.library.common.helper;

import dev.hieplp.library.common.config.OtpConfig;
import dev.hieplp.library.common.entity.Otp;
import dev.hieplp.library.common.enums.IdLength;
import dev.hieplp.library.common.enums.otp.OtpStatus;
import dev.hieplp.library.common.enums.otp.OtpType;
import dev.hieplp.library.common.exception.NotFoundException;
import dev.hieplp.library.common.exception.UnknownException;
import dev.hieplp.library.common.exception.otp.ExceededOtpQuotaException;
import dev.hieplp.library.common.exception.otp.ExpiredOtpException;
import dev.hieplp.library.common.helper.impl.OtpHelperImpl;
import dev.hieplp.library.common.util.DateTimeUtil;
import dev.hieplp.library.common.util.GeneratorUtil;
import dev.hieplp.library.config.AppConfig;
import dev.hieplp.library.repository.OtpRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class OtpHelperTest {

    @InjectMocks
    private OtpHelperImpl otpHelper;

    @Mock
    private AppConfig appConfig;

    @Mock
    private OtpRepository otpRepo;

    @Mock
    private DateTimeUtil dateTimeUtil;
    @Mock
    private GeneratorUtil generatorUtil;

    @BeforeAll
    public static void setup() {
        MockitoAnnotations.openMocks(OtpHelperTest.class);
    }

    @Nested
    class GetOtpConfig {
        private final OtpConfig otpConfig = new OtpConfig();

        GetOtpConfig() {
            otpConfig.setQuota(3);
            otpConfig.setWrongQuota(3);
            otpConfig.setResendQuota(3);
            otpConfig.setExpirationTime(300);
        }

        @Test
        void shouldThrowUnknownException_WhenOtpTypeIsNull() {
            assertThrows(UnknownException.class, () -> otpHelper.getOtpConfig(null));
        }

        @Test
        void shouldThrowUnknownException_WhenOtpTypeIsNotSupported() {
            final var otpType = OtpType.UNKNOWN;
            assertThrows(UnknownException.class, () -> otpHelper.getOtpConfig(otpType));
        }

        @Test
        void shouldReturnRegister_WhenOtpTypeIsRegister() {
            final var otpType = OtpType.REGISTER;
            doReturn(otpConfig).when(appConfig).getRegisterOtp();

            assertEquals(otpConfig, otpHelper.getOtpConfig(otpType));
        }

        @Test
        void shouldReturnForgotPassword_WhenOtpTypeIsForgotPassword() {
            final var otpType = OtpType.FORGOT_PASSWORD;
            doReturn(otpConfig).when(appConfig).getForgotOtp();

            assertEquals(otpConfig, otpHelper.getOtpConfig(otpType));
        }

        @Test
        void shouldReturnVerify_WhenOtpTypeIsVerify() {
            final var otpType = OtpType.VERIFY;
            doReturn(otpConfig).when(appConfig).getVerifyOtp();

            assertEquals(otpConfig, otpHelper.getOtpConfig(otpType));
        }
    }

    @Nested
    class InitOtpTest {
        @Test
        void shouldSuccess_WhenAllConditionsAreMet() {
            final var otpId = "otpId";
            final var otpType = OtpType.REGISTER;
            final var sendTo = "sendTo";
            final var token = "token";
            final var currentTime = new Timestamp(System.currentTimeMillis());
            final var expiryTime = new Timestamp(currentTime.getTime() + 1000L);

            final var otpConfig = new OtpConfig();
            otpConfig.setExpirationTime(1000);

            final var expected = Otp.builder()
                    .otpId(otpId)
                    .type(otpType.getType())
                    .sendTo(sendTo)
                    .token("token")
                    .issueTime(currentTime)
                    .expiryTime(expiryTime)
                    .resendCount(0)
                    .createdAt(currentTime)
                    .modifiedAt(currentTime)
                    .status(OtpStatus.PENDING.getStatus())
                    .build();

            final var spyOtpHelper = Mockito.spy(otpHelper);

            doReturn(otpId).when(generatorUtil).generateId(IdLength.OTP_ID);
            doReturn(otpConfig).when(spyOtpHelper).getOtpConfig(otpType);
            doReturn(token).when(generatorUtil).generateToken();
            doReturn(currentTime).when(dateTimeUtil).getCurrentTimestamp();
            doReturn(currentTime).when(dateTimeUtil).getCurrentTimestamp();
            doReturn(expiryTime).when(dateTimeUtil).addSeconds(currentTime, otpConfig.getExpirationTime());

            assertEquals(expected, spyOtpHelper.initOtp(otpType, sendTo));
        }
    }

    @Nested
    class GetOtpTest {
        final String otpId = "otpId";

        @Test
        void shouldThrowNotFoundException_WhenOtpIsNotFound() {
            doReturn(Optional.empty()).when(otpRepo).findById(otpId);
            assertThrows(NotFoundException.class, () -> otpHelper.getOtp(otpId));
        }

        @Test
        void shouldSuccess_WhenAllConditionsAreMet() {
            final var otp = new Otp()
                    .setOtpId(otpId);
            doReturn(Optional.of(otp)).when(otpRepo).findById(otpId);
            assertEquals(otp, otpHelper.getOtp(otpId));
        }
    }

    @Nested
    class GetOtpByTokenAndTypeTest {
        private final String token = "token";

        @Test
        void shouldThrowNotFoundException_WhenOtpIsNotFound() {
            final var otpType = OtpType.REGISTER;
            doReturn(Optional.empty()).when(otpRepo).findByTokenAndType(token, otpType.getType());
            assertThrows(NotFoundException.class, () -> otpHelper.getOtpByTokenAndType(token, otpType));
        }

        @Test
        void shouldSuccess_WhenAllConditionsAreMet() {
            final var otpType = OtpType.VERIFY;
            final var otp = new Otp()
                    .setToken(token)
                    .setType(otpType.getType());

            doReturn(Optional.of(otp)).when(otpRepo).findByTokenAndType(token, otpType.getType());

            assertEquals(otp, otpHelper.getOtpByTokenAndType(token, otpType));
        }
    }

    @Nested
    class ValidateOtpQuotaTest {
        private final OtpConfig otpConfig = new OtpConfig();
        private final String sendTo = "sendTo";

        ValidateOtpQuotaTest() {
            otpConfig.setQuota(3);
            otpConfig.setWrongQuota(3);
            otpConfig.setResendQuota(3);
            otpConfig.setExpirationTime(300);
        }

        @Test
        void shouldThrowUnknownException_WhenOtpTypeIsNull() {
            assertThrows(UnknownException.class, () -> otpHelper.validateOtpQuota(sendTo, null));
        }

        @Test
        void shouldThrowExceededOtpQuotaException_WhenOtpQuotaIsExceeded() {
            final var spyOtpHelper = Mockito.spy(otpHelper);
            final var otpType = OtpType.REGISTER;

            doReturn(otpConfig).when(spyOtpHelper).getOtpConfig(otpType);
            doReturn(otpConfig.getQuota()).when(otpRepo).countTodayOtpBySendToAndType(sendTo, otpType.getType());

            assertThrows(ExceededOtpQuotaException.class, () -> spyOtpHelper.validateOtpQuota(sendTo, otpType));
        }

        @Test
        void shouldSuccess_WhenAllConditionsAreMet() {
            final var otpType = OtpType.REGISTER;
            final var spyOtpHelper = Mockito.spy(otpHelper);

            doReturn(otpConfig).when(spyOtpHelper).getOtpConfig(otpType);
            doReturn(otpConfig.getQuota() - 1).when(otpRepo).countTodayOtpBySendToAndType(sendTo, otpType.getType());

            spyOtpHelper.validateOtpQuota(sendTo, otpType);
        }
    }

    @Nested
    class ValidateResendOtpQuotaTest {
        private final OtpConfig otpConfig = new OtpConfig();
        private final String otpId = "otpId";

        ValidateResendOtpQuotaTest() {
            otpConfig.setQuota(3);
            otpConfig.setWrongQuota(3);
            otpConfig.setResendQuota(3);
            otpConfig.setExpirationTime(300);
        }

        @Test
        void shouldThrowExceededOtpQuotaException_WhenOtpResendOtpIsExceeded() {
            final var otp = new Otp();
            otp.setResendCount(otpConfig.getResendQuota());
            assertThrows(ExceededOtpQuotaException.class, () -> otpHelper.validateResendOtpQuota(otp, otpConfig));
        }

        @Test
        void shouldSuccess_WhenAllConditionsAreMet() {
            final var otp = new Otp();
            otp.setResendCount(otpConfig.getResendQuota() - 1);
            otpHelper.validateResendOtpQuota(otp, otpConfig);
        }
    }

    /**
     * This class is used to test validateResendOtpQuota(Otp otp, OtpType otpType) method
     */
    @Nested
    class ValidateResendOtpQuotaV2Test {
        private final OtpConfig otpConfig = new OtpConfig();

        ValidateResendOtpQuotaV2Test() {
            otpConfig.setQuota(3);
            otpConfig.setWrongQuota(3);
            otpConfig.setResendQuota(3);
            otpConfig.setExpirationTime(300);
        }

        @Test
        void shouldSuccess_WhenAllConditionsAreMet() {
            final var otp = new Otp();
            otp.setResendCount(otpConfig.getResendQuota() - 1);

            final var otpType = OtpType.REGISTER;
            final var spyOtpHelper = Mockito.spy(otpHelper);

            doReturn(otpConfig).when(spyOtpHelper).getOtpConfig(otpType);

            spyOtpHelper.validateResendOtpQuota(otp, otpType);
        }
    }

    @Nested
    class ValidateOtpLifeTimeTest {
        private final OtpConfig otpConfig = new OtpConfig();

        ValidateOtpLifeTimeTest() {
            otpConfig.setQuota(3);
            otpConfig.setWrongQuota(3);
            otpConfig.setResendQuota(3);
            otpConfig.setExpirationTime(300);
        }

        @Test
        void shouldThrowExpiredOtpException_WhenOtpIsExpired() {
            final var currentTime = new Timestamp(System.currentTimeMillis());
            final var otp = new Otp()
                    .setExpiryTime(new Timestamp(currentTime.getTime() - 1000L));

            doReturn(currentTime).when(dateTimeUtil).getCurrentTimestamp();

            assertThrows(ExpiredOtpException.class, () -> otpHelper.validateOtpLifeTime(otp));
        }

        @Test
        void shouldThrowExpiredOtpException_WhenOtpIsNotPending() {
            final var currentTime = new Timestamp(System.currentTimeMillis());
            final var otp = new Otp()
                    .setExpiryTime(new Timestamp(currentTime.getTime() + 1000L))
                    .setStatus(OtpStatus.EXPIRED.getStatus());

            doReturn(currentTime).when(dateTimeUtil).getCurrentTimestamp();

            assertThrows(ExpiredOtpException.class, () -> otpHelper.validateOtpLifeTime(otp));
        }

        @Test
        void shouldSuccess_WhenAllConditionsAreMet() {
            final var currentTime = new Timestamp(System.currentTimeMillis());
            final var otp = new Otp()
                    .setExpiryTime(new Timestamp(currentTime.getTime() + 1000L))
                    .setStatus(OtpStatus.PENDING.getStatus());

            doReturn(currentTime).when(dateTimeUtil).getCurrentTimestamp();

            otpHelper.validateOtpLifeTime(otp);
        }
    }
}
