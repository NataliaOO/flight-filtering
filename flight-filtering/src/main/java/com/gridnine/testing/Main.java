package com.gridnine.testing;

import java.time.Clock;
import java.time.Duration;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Flight> flights = FlightBuilder.createFlights();

        var nowClock = Clock.systemDefaultZone();
        var rules = List.of(
                new DepartureBeforeNowRule(nowClock),
                new ArrivalBeforeDepartureRule(),
                new ExcessiveGroundTimeRule(Duration.ofHours(2))
        );

        ConsolePrinter.printFlights("Исходный набор перелётов", flights);

        for (ExclusionRule rule : rules) {
            var filtered = FlightFilteringEngine.exclude(flights, rule);
            ConsolePrinter.printFlights("После применения правила: " + rule.description(), filtered);
        }
    }
}
