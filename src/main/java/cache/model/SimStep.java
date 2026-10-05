package cache.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Snapshot after one key access. {@code slots} length equals capacity;
 * null entries are empty.
 */
public record SimStep(
        int key,
        boolean hit,
        Integer evicted,
        List<Integer> slots,
        int hits,
        int misses
) {
    public SimStep {
        slots = Collections.unmodifiableList(new ArrayList<>(slots));
    }
}
