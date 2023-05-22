package com.gen.weather.providers.aggregation;

import com.gen.weather.entitites.MetricStatistic;
import com.gen.weather.exceptions.AggregationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("AVERAGE")
public class AverageMetricAggregationStrategy implements MetricAggregationStrategy {
    @Override
    public Double aggregate(List<Double> metricValues) {
        if (metricValues.size() == 0) {
            throw new AggregationException("You must pass at least one value to get the average.");
        }
        return metricValues.stream().mapToDouble(val -> val).average().orElse(0.0);
    }
}
