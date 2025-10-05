package com.gridnine.testing;

import java.util.List;
import java.util.stream.Collectors;

public final class FlightFilteringEngine {
    private FlightFilteringEngine() {}

    public static List<Flight> exclude(List<Flight> flights, ExclusionRule rule) {
        return flights.stream()
                .filter(f -> !rule.shouldExclude(f))
                .collect(Collectors.toList());
    }
}
