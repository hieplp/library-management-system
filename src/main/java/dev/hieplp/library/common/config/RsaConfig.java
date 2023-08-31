package dev.hieplp.library.common.config;

import lombok.Data;

@Data
public class RsaConfig {
    private String publicKey;
    private String privateKey;
}