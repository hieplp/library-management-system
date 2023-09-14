package dev.hieplp.library.common.helper.impl;

import dev.hieplp.library.common.config.OtpConfig;
import dev.hieplp.library.common.entity.Otp;
import dev.hieplp.library.common.enums.IdLength;
import dev.hieplp.library.common.enums.otp.OtpStatus;
import dev.hieplp.library.common.enums.otp.OtpType;
import dev.hieplp.library.common.exception.NotFoundException;
import dev.hieplp.library.common.exception.UnknownException;
import dev.hieplp.library.common.exception.otp.ExceededOtpQuotaException;
import dev.hieplp.library.common.exception.otp.ExpiredOtpException;
import dev.hieplp.library.common.helper.OtpHelper;
import dev.hieplp.library.common.util.DateTimeUtil;
import dev.hieplp.library.common.util.GeneratorUtil;
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

    private final DateTimeUtil dateTimeUtil;
    private final GeneratorUtil generatorUtil;

    @Override
    public OtpConfig getOtpConfig(OtpType otpType) {
        if (otpType == null) {
            log.warn("otpType is null");
            throw new UnknownException("otpType is null");
        }

        return switch (otpType) {
            case REGISTER -> appConfig.getRegisterOtp();
            case FORGOT_PASSWORD -> appConfig.getForgotOtp();
            case VERIFY -> appConfig.getVerifyOtp();
            default -> throw new UnknownException("Unsupported otpType");
        };
    }

    @Override
    public Otp initOtp(OtpType otpType, String sendTo) {
        log.info("Init otp for {} with sendTo: {}", otpType, sendTo);
        var otpId = generatorUtil.generateId(IdLength.OTP_ID);
        var token = generatorUtil.generateToken();
        var otpConfig = this.getOtpConfig(otpType);
        var currentTime = dateTimeUtil.getCurrentTimestamp();
        var expiryTime = dateTimeUtil.addSeconds(currentTime, otpConfig.getExpirationTime());
        return Otp.builder()
                .otpId(otpId)
                .type(otpType.getType())
                .sendTo(sendTo)
                .token(token)
                .issueTime(currentTime)
                .expiryTime(expiryTime)
                .resendCount(0)
                .createdAt(currentTime)
                .modifiedAt(currentTime)
                .status(OtpStatus.PENDING.getStatus())
                .build();
    }

    @Override
    public Otp getOtp(String otpId) {
        return otpRepo.findById(otpId)
                .orElseThrow(() -> {
                    var message = String.format("Otp id %s not found", otpId);
                    log.warn(message);
                    return new NotFoundException(message);
                });
    }

    @Override
    public Otp getOtpByTokenAndType(String token, OtpType otpType) {
        return otpRepo.findByTokenAndType(token, otpType.getType())
                .orElseThrow(() -> {
                    var message = String.format("Otp with token %s not found", token);
                    log.warn(message);
                    return new NotFoundException(message);
                });
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
        var currentTime = dateTimeUtil.getCurrentTimestamp();
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
