package com.gen.weather.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public final class TimeUtils {
    private TimeUtils() {}

    public static LocalDateTime[] getTimeFromAndTimeTo(LocalDateTime timeFrom, LocalDateTime timeTo) {
        if (timeFrom == null && timeTo == null) {
            LocalDateTime currentTime = java.time.LocalDateTime.now();
            timeTo = currentTime;
            timeFrom = currentTime.minusDays(1);
        } else if (timeFrom == null) {
            timeFrom = timeTo.minusDays(1);
        } else if (timeTo == null) {
            timeTo = timeFrom.plusDays(1);
        }

        validateTime(timeFrom, timeTo);

        return new LocalDateTime[]{timeFrom, timeTo};
    }

    private static void validateTime(LocalDateTime timeFrom, LocalDateTime timeTo) {
        if (!timeFrom.isBefore(timeTo)) {
            throw new IllegalArgumentException("timeFrom must be before timeTo");
        }
        long daysBetweenTimes = timeFrom.until(timeTo, ChronoUnit.DAYS);
        long monthsBetweenTimes = timeFrom.until(timeTo, ChronoUnit.MONTHS);
        if (daysBetweenTimes == 0 || monthsBetweenTimes != 0) {
            throw new IllegalArgumentException("timeFrom and timeTo must be a minimum of 24 hours and a maximum of one month apart.");
        }
    }
}
