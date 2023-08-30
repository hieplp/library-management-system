package dev.hieplp.library.common.enums.response;

public enum ErrorCode implements ResponseCode {
    BAD_REQUEST("4000", "Bad request"),
    DUPLICATED_EMAIL("4001", "Email already exists"),
    DUPLICATED_PHONE("4002", "Phone already exists"),
    DUPLICATED_DEPARTMENT("4003", "Department already exists"),
    NOT_FOUND("4004", "Not found"),
    DUPLICATED_ROLE("4005", "Role already exists"),
    INTERNAL_SERVER_ERROR("5000", "Internal server error"),
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