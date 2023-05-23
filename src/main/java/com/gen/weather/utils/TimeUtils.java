package com.gen.weather.utils;

import java.time.LocalDateTime;

public final class TimeUtils {
    private TimeUtils() {}

    public static LocalDateTime[] getTimeFromAndTimeTo(LocalDateTime timeFrom, LocalDateTime timeTo) {
        if (timeFrom != null && timeTo != null) {
            validateTime(timeFrom, timeTo);
            return new LocalDateTime[]{timeFrom, timeTo};
        }

        if (timeFrom == null && timeTo == null) {
            LocalDateTime currentTime = LocalDateTime.now();
            return new LocalDateTime[]{currentTime.minusDays(1), currentTime};
        }

        if (timeFrom == null) {
            timeFrom = timeTo.minusDays(1);
        } else {
            timeTo = timeFrom.plusDays(1);
        }

        return new LocalDateTime[]{timeFrom, timeTo};
    }

    private static void validateTime(LocalDateTime timeFrom, LocalDateTime timeTo) {
        if (!timeFrom.isBefore(timeTo)) {
            throw new IllegalArgumentException("timeFrom must be before timeTo");
        }

        if (timeTo.minusDays(1).isBefore(timeFrom)) {
            throw new IllegalArgumentException("timeFrom and timeTo cannot be less than 24 hours apart.");
        }

        if (timeFrom.plusMonths(1).isBefore(timeTo)) {
            throw new IllegalArgumentException("timeFrom and timeTo cannot be greater than one calendar month apart.");
        }
    }
}
