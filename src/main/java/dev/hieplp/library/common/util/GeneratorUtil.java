package dev.hieplp.library.common.util;


import dev.hieplp.library.common.enums.IdLength;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.ThreadLocalRandom;

public class GeneratorUtil {
    private static final String CHAR_LIST = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_LIST_NUMBER = "0123456789";
    private static final String CHAR_LIST_UPPER_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Integer SALT_SIZE = 64;
    private static final Integer ITERATION_COUNT = 65536;
    private static final Integer KEY_LENGTH = 64 * 8;


    /**
     * Generate random otp
     *
     * @param length length of OTP
     * @return random OTP
     */
    public static String generateOTP(int length) {
        StringBuilder otp = new StringBuilder();
        // Build the OTP by selecting random characters from the allowedChars string
        for (int i = 0; i < length; i++) {
            int index = ThreadLocalRandom.current().nextInt(CHAR_LIST_NUMBER.length());
            otp.append(CHAR_LIST_NUMBER.charAt(index));
        }

        return otp.toString();
    }

    /**
     * Generate random string with length
     *
     * @param length string length
     * @return random string
     */
    public static String randomString(int length) {
        StringBuilder userId = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = ThreadLocalRandom.current().nextInt(CHAR_LIST_UPPER_ALPHABET.length());
            userId.append(CHAR_LIST_UPPER_ALPHABET.charAt(index));
        }
        return userId.toString();
    }

    /**
     * Generate random id
     *
     * @param idLength id length
     * @return random id
     */
    public static String generateId(IdLength idLength) {
        return randomString(idLength.getLength());
    }

    /**
     * Generate random token
     *
     * @return random token
     */
    public static String generateToken() {
        return randomString(64);
    }

    /**
     * Generate salt
     *
     * @return salt
     */
    public static byte[] generateSalt() {
        final var sr = new SecureRandom();
        byte[] salt = new byte[SALT_SIZE];
        sr.nextBytes(salt);
        return salt;
    }

    /**
     * Hash password with char array password
     *
     * @param password - char array password
     * @param salt     - salt
     * @return Hashed password
     */
    public static byte[] hash(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        var spec = new PBEKeySpec(password, salt, ITERATION_COUNT, KEY_LENGTH);
        var skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return skf.generateSecret(spec).getEncoded();
    }
}
