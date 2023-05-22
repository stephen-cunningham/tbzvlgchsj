package com.gen.weather.providers.aggregation;

import com.gen.weather.exceptions.AggregationException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component("MAX")
public class MaxMetricAggregationStrategy implements MetricAggregationStrategy {
    @Override
    public Double aggregate(List<Double> metricValues) {
        if (metricValues.size() == 0) {
            throw new AggregationException("You must pass at least one value to get the maximum.");
        }
        return Collections.max(metricValues);
    }
}
