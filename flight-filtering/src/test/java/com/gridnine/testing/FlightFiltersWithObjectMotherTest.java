package com.gridnine.testing;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlightFiltersWithObjectMotherTest {

    @Test
    void departureBeforeNow_excludesFlightsWithPastDeparture() {
        var rule = new DepartureBeforeNowRule(TestTime.FIXED);

        var flights = List.of(
                FlightsMother.pastDeparture(),
                FlightsMother.futureSingle()
        );

        var result = FlightFilteringEngine.exclude(flights, rule);

        assertEquals(1, result.size(), "Должен остаться только будущий перелёт");
        assertTrue(result.stream().allMatch(f ->
                f.getSegments().stream().allMatch(s -> !s.getDepartureDate().isBefore(TestTime.NOW))
        ));
    }

    @Test
    void arrivalBeforeDeparture_excludesBrokenSegments() {
        var rule = new ArrivalBeforeDepartureRule();

        var flights = List.of(
                FlightsMother.arrivalBeforeDeparture(),
                FlightsMother.futureSingle()
        );

        var result = FlightFilteringEngine.exclude(flights, rule);

        assertEquals(1, result.size(), "Должен остаться валидный перелёт");
        assertTrue(result.stream().allMatch(f ->
                f.getSegments().stream().noneMatch(s -> s.getArrivalDate().isBefore(s.getDepartureDate()))
        ));
    }

    @Test
    void groundTime_overTwoHours_isExcluded() {
        var rule = new ExcessiveGroundTimeRule(Duration.ofHours(2));

        var overLimit = FlightsMother.withGroundTime(Duration.ofMinutes(130)); // 2:10 -> исключить
        var within   = FlightsMother.withGroundTime(Duration.ofMinutes(110)); // 1:50 -> оставить

        var result = FlightFilteringEngine.exclude(List.of(overLimit, within), rule);

        assertEquals(1, result.size(), "Должен остаться перелёт с пересадкой <= 2 часов");
        assertTrue(result.stream().allMatch(f -> !rule.shouldExclude(f)));
    }

    @Test
    void groundTime_sumAcrossMultipleLayovers_isConsidered() {
        var rule = new ExcessiveGroundTimeRule(Duration.ofHours(2));

        // Пересадки: 1:10 + 1:05 = 2:15 -> исключить
        var overSum = FlightsMother.multiSegment(Duration.ofMinutes(70), Duration.ofMinutes(65));

        // Пересадки: 0:40 + 1:10 = 1:50 -> оставить
        var withinSum = FlightsMother.multiSegment(Duration.ofMinutes(40), Duration.ofMinutes(70));

        var result = FlightFilteringEngine.exclude(List.of(overSum, withinSum), rule);

        assertEquals(1, result.size());
        assertTrue(result.stream().allMatch(f -> !rule.shouldExclude(f)));
    }
}
