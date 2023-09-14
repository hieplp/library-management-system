package dev.hieplp.library.common.helper;

import dev.hieplp.library.common.config.OtpConfig;
import dev.hieplp.library.common.entity.Otp;
import dev.hieplp.library.common.enums.otp.OtpType;

public interface OtpHelper {
    OtpConfig getOtpConfig(OtpType otpType);

    Otp initOtp(OtpType otpType, String sendTo);

    Otp getOtp(String otpId);

    Otp getOtpByTokenAndType(String token, OtpType otpType);

    void validateOtpQuota(String sendTo, OtpType otpType);

    void validateResendOtpQuota(Otp otp, OtpConfig otpConfig);

    void validateResendOtpQuota(Otp otp, OtpType otpType);

    void validateOtpLifeTime(Otp otp);
}
