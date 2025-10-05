package com.gridnine.testing;

import java.time.Clock;
import java.time.LocalDateTime;

public final class DepartureBeforeNowRule implements ExclusionRule {
    private final Clock clock;

    public DepartureBeforeNowRule(Clock clock) {
        this.clock = clock;
    }

    @Override
    public boolean shouldExclude(Flight flight) {
        LocalDateTime now = LocalDateTime.now(clock);
        return flight.getSegments().stream()
                .anyMatch(s -> s.getDepartureDate().isBefore(now));
    }

    @Override
    public String description() {
        return "Исключить перелёты с вылетом до текущего времени";
    }
}
