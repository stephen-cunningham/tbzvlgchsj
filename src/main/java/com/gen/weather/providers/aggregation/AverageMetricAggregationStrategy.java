package com.gen.weather.providers.aggregation;

import com.gen.weather.exceptions.AggregationException;

import java.util.List;

public class AverageMetricAggregationStrategy implements MetricAggregationStrategy {
    @Override
    public Double aggregate(List<Double> metricValues) {
        if (metricValues.size() == 0) {
            throw new AggregationException("You must pass at least one value to get the average.");
        }
        return metricValues.stream().mapToDouble(val -> val).average().orElse(0.0);
    }
}
