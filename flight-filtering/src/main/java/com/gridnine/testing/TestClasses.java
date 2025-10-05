package com.gridnine.testing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Упрощённые модельные классы и фабрика тестовых данных.
 * Всё максимально близко к классическому заданию от Gridnine.
 */
class Segment {
    private final LocalDateTime departureDate;
    private final LocalDateTime arrivalDate;

    Segment(LocalDateTime dep, LocalDateTime arr) {
        this.departureDate = dep;
        this.arrivalDate = arr;
    }

    public LocalDateTime getDepartureDate() { return departureDate; }
    public LocalDateTime getArrivalDate()   { return arrivalDate; }

    @Override
    public String toString() {
        return departureDate + " -> " + arrivalDate;
    }
}

class Flight {
    private final List<Segment> segments;

    Flight(List<Segment> segments) {
        this.segments = segments;
    }

    public List<Segment> getSegments() { return segments; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Flight{");
        for (int i = 0; i < segments.size(); i++) {
            if (i > 0) sb.append(" | ");
            sb.append(segments.get(i).toString());
        }
        sb.append("}");
        return sb.toString();
    }
}

class FlightBuilder {
    /**
     * Базовый набор тестовых перелётов, покрывающий кейсы:
     *  - валидные полёты,
     *  - вылет в прошлом,
     *  - сегмент с прилётом раньше вылета,
     *  - суммарное время на земле > 2 часов.
     */
    static List<Flight> createFlights() {
        LocalDateTime now = LocalDateTime.now();
        List<Flight> flights = new ArrayList<>();

        // 1) Обычный одиночный сегмент (валидный)
        LocalDateTime dep1 = now.plusDays(3).withHour(10).withMinute(0);
        LocalDateTime arr1 = dep1.plusHours(2);
        flights.add(new Flight(Arrays.asList(new Segment(dep1, arr1))));

        // 2) Двухсегментный, пересадка 1 час (валидный)
        LocalDateTime dep2a = now.plusDays(3).withHour(6);
        LocalDateTime arr2a = dep2a.plusHours(2);
        LocalDateTime dep2b = arr2a.plusHours(1);
        LocalDateTime arr2b = dep2b.plusHours(2);
        flights.add(new Flight(Arrays.asList(new Segment(dep2a, arr2a), new Segment(dep2b, arr2b))));

        // 3) Двухсегментный, пересадка 3 часа (должен вылететь по правилу #3)
        LocalDateTime dep3a = now.plusDays(4).withHour(8);
        LocalDateTime arr3a = dep3a.plusHours(2);
        LocalDateTime dep3b = arr3a.plusHours(3);
        LocalDateTime arr3b = dep3b.plusHours(2);
        flights.add(new Flight(Arrays.asList(new Segment(dep3a, arr3a), new Segment(dep3b, arr3b))));

        // 4) Вылет в прошлом (должен вылететь по правилу #1)
        LocalDateTime dep4 = now.minusDays(1).withHour(15);
        LocalDateTime arr4 = dep4.plusHours(2);
        flights.add(new Flight(Arrays.asList(new Segment(dep4, arr4))));

        // 5) Сегмент с прилётом раньше вылета (должен вылететь по правилу #2)
        LocalDateTime dep5 = now.plusDays(2).withHour(12);
        LocalDateTime arr5 = dep5.minusHours(1);
        flights.add(new Flight(Arrays.asList(new Segment(dep5, arr5))));

        // 6) Мультисегментный, пересадки 0:40 и 1:10 (суммарно 1:50, валидный)
        LocalDateTime dep6a = now.plusDays(5).withHour(9);
        LocalDateTime arr6a = dep6a.plusHours(1);
        LocalDateTime dep6b = arr6a.plusMinutes(40);
        LocalDateTime arr6b = dep6b.plusHours(1);
        LocalDateTime dep6c = arr6b.plusMinutes(70);
        LocalDateTime arr6c = dep6c.plusHours(1);
        flights.add(new Flight(Arrays.asList(new Segment(dep6a, arr6a), new Segment(dep6b, arr6b), new Segment(dep6c, arr6c))));

        return flights;
    }
}
