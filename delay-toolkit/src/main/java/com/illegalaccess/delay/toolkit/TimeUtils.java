package com.illegalaccess.delay.toolkit;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Jimmy Li
 * @date 2021-03-04 10:36
 */
public class TimeUtils {

    public static LocalDateTime addTime(Integer delay, TimeUnit timeUnit) {
        return LocalDateTime.now().plus(delay, convert(timeUnit));
    }

    public static long addTimeStamp(Long delay, TimeUnit timeUnit) {
        return toTimeStamp(LocalDateTime.now().plus(delay, convert(timeUnit)));
    }

    public static long toTimeStamp(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static LocalDateTime toLocalDateTime(Long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static long getTimeStamp() {
        return System.currentTimeMillis();
    }

    /**
     * 格式化时间
     *
     * @param localDateTime
     * @return YYYY-mm-dd HH:MM:ss
     */
    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
    }

    public static LocalDateTime parseLocalDateTime(String time) {
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static TemporalUnit convert(TimeUnit tu) {
        if (tu == null) {
            return null;
        }
        switch (tu) {
            case DAYS:
                return ChronoUnit.DAYS;
            case HOURS:
                return ChronoUnit.HOURS;
            case MINUTES:
                return ChronoUnit.MINUTES;
            case SECONDS:
                return ChronoUnit.SECONDS;
            case MICROSECONDS:
                return ChronoUnit.MICROS;
            case MILLISECONDS:
                return ChronoUnit.MILLIS;
            case NANOSECONDS:
                return ChronoUnit.NANOS;
            default:
                assert false : "there are no other TimeUnit ordinal values";
                return null;
        }
    }
}
