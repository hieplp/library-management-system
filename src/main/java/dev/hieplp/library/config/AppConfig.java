package dev.hieplp.library.config;

import dev.hieplp.library.common.config.OtpConfig;
import dev.hieplp.library.common.config.RsaConfig;
import dev.hieplp.library.common.util.EncryptUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.PrivateKey;
import java.security.PublicKey;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    private OtpConfig registerOtp;
    private OtpConfig forgotOtp;
    private RsaConfig passwordRsa;

    @Bean
    public PrivateKey passwordPrivateKey() {
        return EncryptUtil.getPrivateKey(passwordRsa.getPrivateKey());
    }

    @Bean
    public PublicKey passwordPublicKey() {
        return EncryptUtil.getPublicKey(passwordPrivateKey());
    }
}
