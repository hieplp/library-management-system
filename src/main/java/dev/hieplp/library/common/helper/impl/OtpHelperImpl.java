package dev.hieplp.library.common.helper.impl;

import dev.hieplp.library.common.config.OtpConfig;
import dev.hieplp.library.common.entity.Otp;
import dev.hieplp.library.common.enums.otp.OtpStatus;
import dev.hieplp.library.common.enums.otp.OtpType;
import dev.hieplp.library.common.exception.otp.ExceededOtpQuotaException;
import dev.hieplp.library.common.exception.otp.ExpiredOtpException;
import dev.hieplp.library.common.helper.OtpHelper;
import dev.hieplp.library.common.util.DateTimeUtil;
import dev.hieplp.library.config.AppConfig;
import dev.hieplp.library.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OtpHelperImpl implements OtpHelper {

    private final AppConfig appConfig;
    private final OtpRepository otpRepo;

    @Override
    public OtpConfig getOtpConfig(OtpType otpType) {
        return switch (otpType) {
            case REGISTER -> appConfig.getRegisterOtp();
            case FORGOT_PASSWORD -> appConfig.getForgotOtp();
        };
    }

    @Override
    public void validateOtpQuota(String sendTo, OtpType otpType) {
        var otpConfig = getOtpConfig(otpType);
        var todayCount = otpRepo.countTodayOtpBySendToAndType(sendTo, otpType.getType());
        if (todayCount >= otpConfig.getQuota()) {
            log.warn("Exceeded quota for today: {} (Quota: {})", sendTo, otpConfig.getQuota());
            throw new ExceededOtpQuotaException(String.format("Exceed quota for today: %s", sendTo));
        }
    }

    @Override
    public void validateResendOtpQuota(Otp otp, OtpConfig otpConfig) {
        if (otp.getResendCount() >= otpConfig.getResendQuota()) {
            log.warn("Exceeded resend limit: {} (Limit: {})", otp.getOtpId(), otpConfig.getResendQuota());
            throw new ExceededOtpQuotaException(String.format("Exceed resend limit: %s", otp.getOtpId()));
        }
    }

    @Override
    public void validateResendOtpQuota(Otp otp, OtpType otpType) {
        var otpConfig = getOtpConfig(otpType);
        validateResendOtpQuota(otp, otpConfig);
    }

    @Override
    public void validateOtpLifeTime(Otp otp) {
        var currentTime = DateTimeUtil.getCurrentTimestamp();
        if (otp.getExpiryTime().before(currentTime)) {
            log.warn("Otp {} is expired", otp.getOtpId());
            throw new ExpiredOtpException(String.format("Otp %s is expired", otp.getOtpId()));
        }

        if (!OtpStatus.PENDING.getStatus().equals(otp.getStatus())) {
            log.warn("Otp {} is not pending", otp.getOtpId());
            throw new ExpiredOtpException(String.format("Otp %s is not pending", otp.getOtpId()));
        }
    }
}
