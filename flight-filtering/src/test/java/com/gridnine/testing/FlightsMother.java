package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FlightsMother {
    private FlightsMother() {}

    public static Segment seg(LocalDateTime dep, LocalDateTime arr) {
        return new Segment(dep, arr);
    }
    public static Flight flight(Segment... segments) {
        return new Flight(Arrays.asList(segments));
    }

    public static Flight pastDeparture() {
        LocalDateTime dep = TestTime.NOW.minusDays(1).withHour(10).withMinute(0);
        LocalDateTime arr = dep.plusHours(2);
        return flight(seg(dep, arr));
    }

    public static Flight futureSingle() {
        LocalDateTime dep = TestTime.NOW.plusDays(1).withHour(10).withMinute(0);
        LocalDateTime arr = dep.plusHours(2);
        return flight(seg(dep, arr));
    }

    public static Flight arrivalBeforeDeparture() {
        LocalDateTime dep = TestTime.NOW.plusDays(1).withHour(10);
        LocalDateTime arr = dep.minusHours(1);
        return flight(seg(dep, arr));
    }

    public static Flight withGroundTime(Duration ground) {
        LocalDateTime dep1 = TestTime.NOW.plusDays(1).withHour(8);
        LocalDateTime arr1 = dep1.plusHours(1);
        LocalDateTime dep2 = arr1.plus(ground);
        LocalDateTime arr2 = dep2.plusHours(1);
        return flight(seg(dep1, arr1), seg(dep2, arr2));
    }

    public static Flight multiSegment(Duration... grounds) {
        LocalDateTime dep = TestTime.NOW.plusDays(1).withHour(8);
        LocalDateTime arr = dep.plusHours(1);

        List<Segment> segments = new ArrayList<>();
        segments.add(seg(dep, arr));

        for (Duration g : grounds) {
            dep = arr.plus(g);
            arr = dep.plusHours(1);
            segments.add(seg(dep, arr));
        }
        return flight(segments.toArray(new Segment[0]));
    }
}
