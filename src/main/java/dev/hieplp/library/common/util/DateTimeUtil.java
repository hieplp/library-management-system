package dev.hieplp.library.common.util;

import java.sql.Timestamp;

public class DateTimeUtil {
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Timestamp addSeconds(Timestamp timestamp, int seconds) {
        return new Timestamp(timestamp.getTime() + seconds * 1000L);
    }
}
