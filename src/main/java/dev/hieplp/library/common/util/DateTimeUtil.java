package dev.hieplp.library.common.util;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

@Component
public class DateTimeUtil {
    public Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public Date getCurrentDate() {
        return new Date();
    }

    public Timestamp addSeconds(Timestamp timestamp, int seconds) {
        return new Timestamp(timestamp.getTime() + seconds * 1000L);
    }

    public Date addSeconds(Date date, int seconds) {
        return new Date(date.getTime() + seconds * 1000L);
    }
}
