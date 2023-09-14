package dev.hieplp.library.payload.response.auth.verify;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ResendVerifyOtpResponse extends RequestToVerifyResponse {
    private Integer resendCount;
    private Integer resendQuota;
}
