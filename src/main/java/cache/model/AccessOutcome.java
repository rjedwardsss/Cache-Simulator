package cache.model;

import java.util.OptionalInt;

public record AccessOutcome(boolean hit, OptionalInt evictedKey) {
    public static AccessOutcome ofHit() {
        return new AccessOutcome(true, OptionalInt.empty());
    }

    public static AccessOutcome ofMiss() {
        return new AccessOutcome(false, OptionalInt.empty());
    }

    public static AccessOutcome ofMissEvict(int key) {
        return new AccessOutcome(false, OptionalInt.of(key));
    }
}
