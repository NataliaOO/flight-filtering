package com.gridnine.testing;

import java.time.*;

public final class TestTime {
    public static final ZoneId ZONE = ZoneId.of("UTC");
    public static final Clock FIXED = Clock.fixed(Instant.parse("2025-10-05T12:00:00Z"), ZONE);
    public static final LocalDateTime NOW = LocalDateTime.now(FIXED);
    private TestTime() {}
}
