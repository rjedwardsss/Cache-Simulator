package cache.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Shared access traces (parity with Portfolio/lib/cache-simulator.ts). */
public final class TracePresets {
    public enum Id {
        HOT_COLD,
        FREQUENCY_SKEW,
        THRASH
    }

    public record Preset(String name, List<Integer> keys) {}

    private static final Map<Id, Preset> PRESETS = new LinkedHashMap<>();

    static {
        PRESETS.put(Id.HOT_COLD, new Preset("Hot-cold", List.of(1, 2, 3, 1, 4, 1, 5)));
        PRESETS.put(Id.FREQUENCY_SKEW, new Preset("Frequency skew", List.of(1, 1, 1, 2, 2, 3, 4)));
        PRESETS.put(Id.THRASH, new Preset("Thrash", List.of(1, 2, 3, 4, 1, 2, 3, 4, 1)));
    }

    public static Preset get(Id id) {
        Preset p = PRESETS.get(id);
        if (p == null) {
            throw new IllegalArgumentException("Unknown preset: " + id);
        }
        return p;
    }

    public static Id[] ids() {
        return Id.values();
    }

    private TracePresets() {}
}
