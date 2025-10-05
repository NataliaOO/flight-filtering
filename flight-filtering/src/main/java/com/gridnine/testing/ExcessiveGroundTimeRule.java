package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public final class ExcessiveGroundTimeRule implements ExclusionRule {
    private final Duration maxGroundTime;

    public ExcessiveGroundTimeRule(Duration maxGroundTime) {
        this.maxGroundTime = maxGroundTime;
    }

    @Override
    public boolean shouldExclude(Flight flight) {
        List<Segment> segs = flight.getSegments();
        if (segs.size() < 2) return false;

        Duration total = Duration.ZERO;
        for (int i = 0; i < segs.size() - 1; i++) {
            LocalDateTime prevArr = segs.get(i).getArrivalDate();
            LocalDateTime nextDep = segs.get(i + 1).getDepartureDate();
            if (nextDep.isAfter(prevArr)) {
                total = total.plus(Duration.between(prevArr, nextDep));
            }
        }
        return total.compareTo(maxGroundTime) > 0;
    }

    @Override
    public String description() {
        return "Исключить перелёты с суммарным временем на земле больше " + maxGroundTime;
    }
}
