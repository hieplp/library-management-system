package dev.hieplp.library.common.enums.response;

public enum ErrorCode implements ResponseCode {
    BAD_REQUEST("4000", "Bad request"),
    UNAUTHORIZED("4001", "Unauthorized"),
    NOT_FOUND("4004", "Not found"),
    INTERNAL_SERVER_ERROR("5000", "Internal server error"),

    // OTP
    OTP_WRONG("OTP_4001", "Wrong OTP"),
    OTP_EXPIRED("OTP_4002", "OTP expired"),
    OTP_QUOTA_EXCEEDED("OTP_4003", "OTP quota exceeded"),
    OTP_IS_USED("OTP_4004", "OTP is used"),

    // USER
    USER_DUPLICATED_EMAIL("USER_4001", "Email already exists"),
    USER_DUPLICATE_USERNAME("USER_4002", "Username already exists"),
    USER_INVALID_PASSWORD_OR_USERNAME("USER_4003", "Invalid password or username"),
    ;

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}