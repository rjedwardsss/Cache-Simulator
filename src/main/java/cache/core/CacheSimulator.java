package cache.core;

import cache.model.AccessOutcome;
import cache.model.Policy;
import cache.model.SimStep;
import cache.model.SimulationResult;

import java.util.ArrayList;
import java.util.List;

public interface CacheSimulator {
    Policy policy();

    int capacity();

    AccessOutcome access(int key);

    /** Slot keys in insertion order (null pads unused capacity). */
    List<Integer> snapshotSlots();

    void reset();

    default SimulationResult runTrace(List<Integer> keys) {
        reset();
        List<SimStep> steps = new ArrayList<>();
        int hits = 0;
        int misses = 0;
        for (int key : keys) {
            AccessOutcome outcome = access(key);
            if (outcome.hit()) {
                hits++;
            } else {
                misses++;
            }
            Integer evicted = outcome.evictedKey().isPresent()
                    ? outcome.evictedKey().getAsInt()
                    : null;
            steps.add(new SimStep(key, outcome.hit(), evicted, snapshotSlots(), hits, misses));
        }
        return new SimulationResult(steps, hits, misses);
    }
}
