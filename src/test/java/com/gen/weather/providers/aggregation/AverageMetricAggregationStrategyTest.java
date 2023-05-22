package com.gen.weather.providers.aggregation;

import com.gen.weather.exceptions.AggregationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AverageMetricAggregationStrategyTest {
    private AverageMetricAggregationStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new AverageMetricAggregationStrategy();
    }

    @Test
    void aggregate_ListOfDouble_ShouldGetMaxValue() {
        List<Double> numsToAggregate = List.of(9.0, 9.1, -5.2, -5.21, 2.0, 2.01);
        assertEquals(1.95, strategy.aggregate(numsToAggregate));
    }

    @Test
    void aggregate_ListOfZero_ReturnZero() {
        List<Double> numsToAggregate = List.of(0.0, 0.0);
        assertEquals(0.0, strategy.aggregate(numsToAggregate));
    }

    @Test
    void aggregate_EmptyList_ThrowError() {
        Throwable throwable = assertThrows(AggregationException.class, () -> strategy.aggregate(Collections.emptyList()));
        assertEquals("You must pass at least one value to get the average.", throwable.getMessage());
    }
}