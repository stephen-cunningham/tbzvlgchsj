package com.gen.weather.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TimeUtilsTest {
    @Test
    void getTimeFromAndTimeTo_BothNull_ReturnLast24Hours() {
        LocalDateTime timeBeforeCall = LocalDateTime.now().minusNanos(1);
        LocalDateTime[] times = TimeUtils.getTimeFromAndTimeTo(null, null);
        LocalDateTime timeAfterCall = LocalDateTime.now().plusNanos(1);

        assertAll(
                () -> assertTrue(times[1].isAfter(timeBeforeCall) && times[1].isBefore(timeAfterCall)),
                () -> assertEquals(times[0].plusDays(1), times[1])
        );
    }

    @Test
    void getTimeFromAndTimeTo_TimeFromNull_ReturnRange24HoursPriorToTimeTo() {
        LocalDateTime timeTo = LocalDateTime.of(2023, 1, 2, 1, 2, 3);
        LocalDateTime expectedTimeFrom = timeTo.minusDays(1);
        LocalDateTime[] times = TimeUtils.getTimeFromAndTimeTo(null, timeTo);

        LocalDateTime returnedTimeFrom = times[0];
        LocalDateTime returnedTimeTo = times[1];

        assertAll(
                () -> assertEquals(timeTo, returnedTimeTo),
                () -> assertEquals(expectedTimeFrom, returnedTimeFrom)
        );
    }

    @Test
    void getTimeFromAndTimeTo_TimeToNull_ReturnRange24HoursAfterTimeFor() {
        LocalDateTime timeFrom = LocalDateTime.of(2023, 1, 1, 1, 2, 3);
        LocalDateTime expectedTimeTo = timeFrom.plusDays(1);
        LocalDateTime[] times = TimeUtils.getTimeFromAndTimeTo(timeFrom, null);

        LocalDateTime returnedTimeFrom = times[0];
        LocalDateTime returnedTimeTo = times[1];

        assertAll(
                () -> assertEquals(timeFrom, returnedTimeFrom),
                () -> assertEquals(expectedTimeTo, returnedTimeTo)
        );
    }

    @ParameterizedTest
    @MethodSource("timeFromTimeToValidRanges")
    void getTimeFromAndTimeTo_ValidRanges_ReturnBoth(LocalDateTime timeFrom, LocalDateTime timeTo) {
        LocalDateTime[] times = TimeUtils.getTimeFromAndTimeTo(timeFrom, timeTo);

        assertAll(
                () -> assertEquals(timeFrom, times[0]),
                () -> assertEquals(timeTo, times[1])
        );
    }

    static Stream<Arguments> timeFromTimeToValidRanges() {
        LocalDateTime firstJan = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
        LocalDateTime firstFebLeapYear = LocalDateTime.of(2024, 2, 1, 0, 0, 0, 0);
        LocalDateTime firstFebNonLeapYear = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);
        LocalDateTime firstApril = LocalDateTime.of(2023, 4, 1, 0, 0, 0, 0);

        return Stream.of(
                Arguments.arguments(firstJan, firstJan.plusDays(1)),
                Arguments.arguments(firstJan, firstJan.plusMonths(1)),
                Arguments.arguments(firstFebLeapYear, firstFebLeapYear.plusMonths(1)),
                Arguments.arguments(firstFebNonLeapYear, firstFebNonLeapYear.plusMonths(1)),
                Arguments.arguments(firstApril, firstApril.plusMonths(1))
        );
    }

    @ParameterizedTest
    @MethodSource("timeFromTimeToInvalidRanges")
    void getTimeFromAndTimeTo_TimeFromTimeToDiffIncorrect_ThrowError(
            LocalDateTime timeFrom, LocalDateTime timeTo, String expectedErrorMessage) {
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> TimeUtils.getTimeFromAndTimeTo(timeFrom, timeTo));
        assertEquals(expectedErrorMessage, throwable.getMessage());
        System.out.println(LocalDateTime.of(2021, 1, 1, 1, 1, 1, 1).plusMonths(1).isBefore(LocalDateTime.of(2021, 2, 1, 1, 1, 1, 1)));
    }

    static Stream<Arguments> timeFromTimeToInvalidRanges() {
        String fromBeforeTo = "timeFrom must be before timeTo";
        String moreThanOneMonthApart = "timeFrom and timeTo cannot be greater than one calendar month apart.";

        LocalDateTime firstJan = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
        LocalDateTime firstFebLeapYear = LocalDateTime.of(2024, 2, 1, 0, 0, 0, 0);
        LocalDateTime firstFebNonLeapYear = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);
        LocalDateTime firstApril = LocalDateTime.of(2023, 4, 1, 0, 0, 0, 0);

        return Stream.of(
                Arguments.arguments(firstJan, firstJan, fromBeforeTo),
                Arguments.arguments(firstJan.plusNanos(1), firstJan, fromBeforeTo),
                Arguments.arguments(firstJan, firstJan.plusDays(1).minusNanos(1), "timeFrom and timeTo cannot be less than 24 hours apart."),
                Arguments.arguments(firstJan, firstJan.plusMonths(2), moreThanOneMonthApart),
                Arguments.arguments(firstJan, firstJan.plusYears(1), moreThanOneMonthApart),
                Arguments.arguments(firstJan, firstJan.plusMonths(1).plusNanos(1), moreThanOneMonthApart),
                Arguments.arguments(firstFebLeapYear, firstFebLeapYear.plusMonths(1).plusNanos(1), moreThanOneMonthApart),
                Arguments.arguments(firstFebNonLeapYear, firstFebNonLeapYear.plusMonths(1).plusNanos(1), moreThanOneMonthApart),
                Arguments.arguments(firstApril, firstApril.plusMonths(1).plusNanos(1), moreThanOneMonthApart)
        );
    }
}