package com.gen.weather.providers.aggregation;

import java.util.List;

public interface MetricAggregationStrategy {
    Double aggregate(List<Double> metricValues);
}
