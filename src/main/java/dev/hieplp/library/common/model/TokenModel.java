package dev.hieplp.library.common.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class TokenModel {
    private String token;
    private Date expiredAt;
}
