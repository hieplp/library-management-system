package dev.hieplp.library.common.util;

import dev.hieplp.library.common.exception.UnknownException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.Base64;


@Slf4j
@Component
@RequiredArgsConstructor
public class EncryptUtil {

    private final GeneratorUtil generatorUtil;

    public static PrivateKey getPrivateKey(String filePath) {
        try {
            // Private key
            var privateBytes = FileUtil.decodeByteFile(filePath);
            var privateSpec = new PKCS8EncodedKeySpec(privateBytes);
            var kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(privateSpec);
        } catch (Exception e) {
            log.error("Error when get private key caused by {}", e.getMessage());
            throw new UnknownException(e.getMessage());
        }
    }

    public static PublicKey getPublicKey(PrivateKey privateKey) {
        try {
            var kf = KeyFactory.getInstance("RSA");
            var rsaPrivateCrtKey = (RSAPrivateCrtKey) privateKey;
            var publicSpec = new RSAPublicKeySpec(rsaPrivateCrtKey.getModulus(), rsaPrivateCrtKey.getPublicExponent());
            return kf.generatePublic(publicSpec);
        } catch (Exception e) {
            log.error("Error when get public key caused by {}", e.getMessage());
            throw new UnknownException(e.getMessage());
        }
    }

    public byte[] decrypt(byte[] buffer, PrivateKey privateKey) {
        try {
            Cipher rsa = Cipher.getInstance("RSA");
            rsa.init(Cipher.DECRYPT_MODE, privateKey);
            return rsa.doFinal(buffer);
        } catch (Exception e) {
            log.error("Error when decrypt RSA string caused by {}", e.getMessage());
            throw new UnknownException("Error on decrypt password.");
        }
    }

    public byte[] generatePassword(String password, PrivateKey privateKey, byte[] salt) {
        try {
            log.info("Start generate password");
            log.info("Pasword:{}", password);
            byte[] rawPassword = decrypt(Base64.getDecoder().decode(password), privateKey);
            rawPassword = generatorUtil.hash(ConverterUtil.toCharArray(rawPassword), salt);
            return rawPassword;
        } catch (Exception e) {
            throw new UnknownException(e.getMessage());
        }
    }

    public boolean validatePassword(String inputPassword, byte[] userPassword, byte[] salt, PrivateKey privateKey) {
        log.info("Start validate password");
        byte[] rawPassword = new byte[0];
        try {
            rawPassword = generatePassword(inputPassword, privateKey, salt);
            return Arrays.equals(rawPassword, userPassword);
        } catch (Exception e) {
            log.error("Error when validate password caused by {}", e.getMessage());
            return false;
        } finally {
            // Clear password in memory for security
            Arrays.fill(rawPassword, Byte.MIN_VALUE);
            // TODO: It will update password in database. Need to fix
            // Arrays.fill(userPassword, Byte.MIN_VALUE);
        }
    }
}
