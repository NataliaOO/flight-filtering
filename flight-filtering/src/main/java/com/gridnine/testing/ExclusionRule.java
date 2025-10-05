package com.gridnine.testing;

public interface ExclusionRule {
    /** @return true — перелёт должен быть исключён по этому правилу */
    boolean shouldExclude(Flight flight);

    /** Человеко-читаемое описание правила (для вывода) */
    String description();
}
