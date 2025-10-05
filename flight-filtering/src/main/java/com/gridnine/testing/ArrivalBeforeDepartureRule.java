package com.gridnine.testing;

public final class ArrivalBeforeDepartureRule implements ExclusionRule {
    @Override
    public boolean shouldExclude(Flight flight) {
        return flight.getSegments().stream()
                .anyMatch(s -> s.getArrivalDate().isBefore(s.getDepartureDate()));
    }

    @Override
    public String description() {
        return "Исключить перелёты, где есть сегмент с прилётом раньше вылета";
    }
}
