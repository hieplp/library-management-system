package dev.hieplp.library.common.util;

import dev.hieplp.library.common.exception.UnknownException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;


@Slf4j
public class EmailUtil {
    public static void sendMime(JavaMailSender sender,
                                String sendTo,
                                String subject,
                                String content) {
        try {
            log.info("Send mime email to {} with subject {} and content {}", sendTo, subject, content);
            var message = sender.createMimeMessage();
            var helper = new MimeMessageHelper(message, true);
            helper.setTo(sendTo);
            helper.setSubject(subject);
            helper.setText(content);
            sender.send(message);
        } catch (Exception e) {
            log.error("Error when send mime email caused by {}", e.getMessage());
            throw new UnknownException("Error when send mime email.");
        }
    }
}