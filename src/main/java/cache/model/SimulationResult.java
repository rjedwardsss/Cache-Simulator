package cache.model;

import java.util.List;

public record SimulationResult(List<SimStep> steps, int hits, int misses) {
    public SimulationResult {
        steps = List.copyOf(steps);
    }

    public double hitRate() {
        int total = hits + misses;
        return total == 0 ? 0.0 : (double) hits / total;
    }
}
