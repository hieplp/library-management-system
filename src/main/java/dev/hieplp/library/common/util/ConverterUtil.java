package dev.hieplp.library.common.util;

public class ConverterUtil {
    public static char[] toCharArray(byte[] bytes) {
        // Using the platform's default charset
        String str = new String(bytes);
        return str.toCharArray();
    }
}
