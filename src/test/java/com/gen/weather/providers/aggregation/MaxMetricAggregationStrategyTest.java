package com.gen.weather.providers.aggregation;

import com.gen.weather.exceptions.AggregationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MaxMetricAggregationStrategyTest {
    private MaxMetricAggregationStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new MaxMetricAggregationStrategy();
    }

    @Test
    void aggregate_ListOfDouble_GetMaxValue() {
        List<Double> numsToAggregate = List.of(9.0, 9.000001, -5.2, -5.21, 2.0, 2.01);
        assertEquals(9.000001, strategy.aggregate(numsToAggregate));
    }

    @Test
    void aggregate_EmptyList_ThrowError() {
        Throwable throwable = assertThrows(AggregationException.class, () -> strategy.aggregate(Collections.emptyList()));
        assertEquals("You must pass at least one value to get the maximum.", throwable.getMessage());
    }
}