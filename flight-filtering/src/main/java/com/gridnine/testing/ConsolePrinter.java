package com.gridnine.testing;

import java.util.List;

public final class ConsolePrinter {
    private ConsolePrinter() {}

    public static void printFlights(String title, List<Flight> flights) {
        System.out.println("=== " + title + " ===");
        if (flights.isEmpty()) {
            System.out.println("(пусто)");
        } else {
            for (int i = 0; i < flights.size(); i++) {
                System.out.println((i + 1) + ") " + flights.get(i));
            }
        }
        System.out.println();
    }
}
