package com.gen.weather.services;

import com.gen.weather.entitites.MetricStatistic;
import com.gen.weather.entitites.MetricType;
import com.gen.weather.entitites.WeatherDataPoint;
import com.gen.weather.entitites.WeatherSensor;
import com.gen.weather.exceptions.NotFoundException;
import com.gen.weather.providers.aggregation.*;
import com.gen.weather.repositories.WeatherDataPointRepository;
import com.gen.weather.repositories.WeatherSensorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherDataPointServiceImplTest {
    private static final Map<String, MetricAggregationStrategy> METRIC_AGGREGATION_STRATEGY_MAP = Map.of(
            "AVERAGE", new AverageMetricAggregationStrategy(),
            "MAXIMUM", new MaxMetricAggregationStrategy(),
            "MINIMUM", new MinMetricAggregationStrategy(),
            "SUM", new SumMetricAggregationStrategy());
    private WeatherDataPointServiceImpl weatherDataPointService;


    @Mock private WeatherDataPointRepository weatherDataPointRepository;
    @Mock private WeatherSensorRepository weatherSensorRepository;
    @Mock private WeatherSensor weatherSensor;

    @Captor private ArgumentCaptor<LocalDateTime> localDateTimeArgumentCaptor;
    @Captor private ArgumentCaptor<WeatherDataPoint> weatherDataPointArgumentCaptor;

    @BeforeEach
    void setUp() {
        weatherDataPointService = new WeatherDataPointServiceImpl(
                weatherDataPointRepository, weatherSensorRepository, METRIC_AGGREGATION_STRATEGY_MAP);
    }

    @ParameterizedTest
    @MethodSource("getTimeToLessThanOrEqualToTimeFrom")
    void query_TimeToLessThanOrEqualToTimeTimeFrom_ThrowError(LocalDateTime timeTo) {
        LocalDateTime timeFrom = LocalDateTime.of(2023, 1, 1, 1, 1, 1, 2);

        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> weatherDataPointService.query(null, timeFrom, timeTo, null, null));
        assertAll(
                () -> assertEquals("timeFrom must be before timeTo", throwable.getMessage()),
                () -> verifyNoInteractions(weatherDataPointRepository)
        );
    }

    private static Stream<Arguments> getTimeToLessThanOrEqualToTimeFrom() {
        return Stream.of(
                Arguments.arguments(LocalDateTime.of(2023, 1, 1, 1, 1, 1, 1)),
                Arguments.arguments(LocalDateTime.of(2023, 1, 1, 1, 1, 1, 2))
        );
    }

    @ParameterizedTest
    @MethodSource("getTimeToDifferenceLessThanDayGreaterThanMonth")
    void query_DifferenceBetweenTimeFromAndTimeToLessThanOneDayOrGreaterThanOneMonth_ThrowError(LocalDateTime timeTo) {
        LocalDateTime timeFrom = LocalDateTime.of(2023, 1, 1, 1, 1, 1, 2);

        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> weatherDataPointService.query(null, timeFrom, timeTo, null, null));
        assertAll(
                () -> assertEquals("timeFrom and timeTo must be a minimum of 24 hours and a maximum of one month apart.", throwable.getMessage()),
                () -> verifyNoInteractions(weatherDataPointRepository)
        );
    }

    private static Stream<Arguments> getTimeToDifferenceLessThanDayGreaterThanMonth() {
        return Stream.of(
                Arguments.arguments(LocalDateTime.of(2023, 1, 1, 1, 1, 1, 5)),
                Arguments.arguments(LocalDateTime.of(2023, 2, 2, 1, 1, 1, 2))
        );
    }

    @Test
    void query_TimeFromNullAndTimeToNull_AreAutomaticallySet() {
        when(weatherDataPointRepository.filterMetricValuesByParams(any(), any(), any(), any())).thenReturn(List.of(2.0));

        LocalDateTime timeBeforeCall = LocalDateTime.now().minusSeconds(1);
        weatherDataPointService.query(null, null, null, MetricStatistic.AVERAGE, null);
        LocalDateTime timeAfterCall = LocalDateTime.now().plusSeconds(1);

        assertAll(
                () -> verify(weatherDataPointRepository).filterMetricValuesByParams(
                        any(), any(), localDateTimeArgumentCaptor.capture(), localDateTimeArgumentCaptor.capture()),
                () -> verifyNoMoreInteractions(weatherDataPointRepository),
                () -> {
                    LocalDateTime capturedTimeFrom = localDateTimeArgumentCaptor.getAllValues().get(0);
                    LocalDateTime capturedTimeTo = localDateTimeArgumentCaptor.getAllValues().get(1);
                    assertAll(
                            () -> assertTrue(capturedTimeTo.isAfter(timeBeforeCall) && capturedTimeTo.isBefore(timeAfterCall)),
                            () -> assertEquals(1, ChronoUnit.DAYS.between(capturedTimeFrom, capturedTimeTo))
                    );
                }
        );
    }

    @Test
    void query_TimeFromNullAndTimeToSet_TimeFromAutomaticallySet() {
        when(weatherDataPointRepository.filterMetricValuesByParams(any(), any(), any(), any())).thenReturn(List.of(2.0));
        LocalDateTime timeTo = LocalDateTime.of(2023, 1, 2, 1, 1, 1, 1);

        weatherDataPointService.query(null, null, timeTo, MetricStatistic.AVERAGE, null);

        assertAll(
                () -> verify(weatherDataPointRepository).filterMetricValuesByParams(
                        any(), any(), localDateTimeArgumentCaptor.capture(), localDateTimeArgumentCaptor.capture()),
                () -> verifyNoMoreInteractions(weatherDataPointRepository),
                () -> {
                    LocalDateTime capturedTimeFrom = localDateTimeArgumentCaptor.getAllValues().get(0);
                    LocalDateTime capturedTimeTo = localDateTimeArgumentCaptor.getAllValues().get(1);
                    assertAll(
                            () -> assertEquals(timeTo.minusDays(1), capturedTimeFrom),
                            () -> assertEquals(1, ChronoUnit.DAYS.between(capturedTimeFrom, capturedTimeTo))
                    );
                }
        );
    }

    @Test
    void query_TimeFromSetAndTimeToNull_TimeToAutomaticallySet() {
        when(weatherDataPointRepository.filterMetricValuesByParams(any(), any(), any(), any())).thenReturn(List.of(2.0));
        LocalDateTime timeFrom = LocalDateTime.of(2023, 1, 2, 1, 1, 1, 1);

        weatherDataPointService.query(null, timeFrom, null, MetricStatistic.AVERAGE, null);

        assertAll(
                () -> verify(weatherDataPointRepository).filterMetricValuesByParams(
                        any(), any(), localDateTimeArgumentCaptor.capture(), localDateTimeArgumentCaptor.capture()),
                () -> verifyNoMoreInteractions(weatherDataPointRepository),
                () -> {
                    LocalDateTime capturedTimeFrom = localDateTimeArgumentCaptor.getAllValues().get(0);
                    LocalDateTime capturedTimeTo = localDateTimeArgumentCaptor.getAllValues().get(1);
                    assertAll(
                            () -> assertEquals(timeFrom.plusDays(1), capturedTimeTo),
                            () -> assertEquals(1, ChronoUnit.DAYS.between(capturedTimeFrom, capturedTimeTo))
                    );
                }
        );
    }

    @Test
    void query_NoDataFound_ThrowsError() {
        when(weatherDataPointRepository.filterMetricValuesByParams(any(), any(), any(), any())).thenReturn(Collections.emptyList());

        Throwable throwable = assertThrows(NotFoundException.class, () -> weatherDataPointService.query(
                null, null, null, MetricStatistic.AVERAGE, MetricType.HUMIDITY));
        assertEquals("Weather Data Points not found for the HUMIDITY metric for the following weather sensors: null", throwable.getMessage());
    }

    @Test
    void query_IdsProvidedAndResultsReturned_CallsRepositoryMultipleTimesAndNoErrors() {
        when(weatherDataPointRepository.filterMetricValuesByParams(any(), any(), any(), any())).thenReturn(List.of(2.0));

        List<UUID> ids = List.of(UUID.fromString("1024baf2-12d4-4939-8071-38ce15378e5a"), UUID.fromString("b9c89276-73f6-425c-9451-ac5c79d3bc36"));

        assertAll(
                () -> assertDoesNotThrow(() -> weatherDataPointService.query(ids, null, null, MetricStatistic.AVERAGE, null)),
                () -> verify(weatherDataPointRepository, times(2)).filterMetricValuesByParams(any(), any(), any(), any())
        );
    }

    @Test
    void save_WeatherSensorNotFound_ThrowError() {
        when(weatherSensorRepository.findById(any())).thenReturn(Optional.empty());
        UUID uuid = UUID.randomUUID();

        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> weatherDataPointService.save(uuid, null, null));
        assertAll(
                () -> assertEquals("Weather Sensor does not exist with id " + uuid, throwable.getMessage()),
                () -> verifyNoInteractions(weatherDataPointRepository)
        );
    }

    @Test
    void save_WeatherSensorFound_Save() {
        UUID uuid = UUID.randomUUID();
        MetricType metricType = MetricType.HUMIDITY;
        Double metricValue = 20.0;

        when(weatherSensorRepository.findById(any())).thenReturn(Optional.of(weatherSensor));

        weatherDataPointService.save(uuid, metricType, metricValue);

        verify(weatherDataPointRepository).save(weatherDataPointArgumentCaptor.capture());
        WeatherDataPoint capturedWeatherDataPoint = weatherDataPointArgumentCaptor.getValue();
        assertAll(
                () -> assertEquals(uuid, capturedWeatherDataPoint.getWeatherSensorId()),
                () -> assertEquals(metricType, capturedWeatherDataPoint.getMetricType()),
                () -> assertEquals(metricValue, capturedWeatherDataPoint.getMetricValue())
        );
    }
}