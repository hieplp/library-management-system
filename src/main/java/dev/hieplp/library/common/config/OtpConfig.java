package dev.hieplp.library.common.config;

import lombok.Data;

@Data
public class OtpConfig {
    /**
     * Count of OTP that can be sent to a phone number in a day
     */
    private int quota;

    /**
     * Count of wrong OTP that can be entered before the OTP is expired
     */
    private int wrongQuota;

    /**
     * Count of OTP that can be resent to a phone number in a session
     */
    private int resendQuota;

    /**
     * Count of seconds before the OTP is expired
     */
    private int expirationTime;
}
