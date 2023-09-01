package dev.hieplp.library.common.util;

import org.springframework.stereotype.Component;

@Component
public class MaskUtil {
    public String maskEmail(String email) {
        var atIndex = email.indexOf('@');
        if (atIndex > 1) { // Check if "@" is at least the second character
            return email.charAt(0) + // Keep the first character
                    "*".repeat(atIndex - 1) + // Mask characters between the first character and "@"
                    email.substring(atIndex);
        } else {
            return email; // Invalid email format, return original email
        }
    }
}