package dev.hieplp.library.common.util;

import dev.hieplp.library.config.security.CurrentUser;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class CurrentUserUtil {

    private final CurrentUser currentUser;

    public CurrentUserUtil(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    public String getUserId() {
        return currentUser.getUserId();
    }


    public String getToken() {
        return currentUser.getToken();
    }

    public Byte getTokenType() {
        return currentUser.getTokenType();
    }
}
