package dev.hieplp.library.common.payload.response;

import dev.hieplp.library.common.enums.response.ResponseCode;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class CommonResponse {
    private final String code;
    private final Object data;
    private final String message;

    public CommonResponse(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.data = new HashMap<>();
    }

    public CommonResponse(ResponseCode response, Object data) {
        this.code = response.getCode();
        this.message = response.getMessage();
        this.data = data;
    }

    public CommonResponse(ResponseCode responseCode, String message) {
        this.code = responseCode.getCode();
        this.message = message;
        this.data = new HashMap<>();
    }
}
