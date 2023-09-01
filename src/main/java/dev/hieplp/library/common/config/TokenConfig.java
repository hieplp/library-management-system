package dev.hieplp.library.common.config;

import lombok.Data;

@Data
public class TokenConfig {
    /**
     * Time to live of token in seconds
     */
    private Integer activeTime;

    /**
     * Issuer of token
     */
    private String issuer;
}
