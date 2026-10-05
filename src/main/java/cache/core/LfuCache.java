package cache.core;

import cache.model.AccessOutcome;
import cache.model.CacheEntry;
import cache.model.Policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fully associative LFU cache. Evicts lowest frequency; ties broken by least
 * recently used (smallest lastUsed tick).
 */
public final class LfuCache implements CacheSimulator {
    private final int capacity;
    private final Map<Integer, CacheEntry> entries = new HashMap<>();
    private final List<Integer> order = new ArrayList<>();
    private long tick = 0;

    public LfuCache(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity must be >= 1");
        }
        this.capacity = capacity;
    }

    @Override
    public Policy policy() {
        return Policy.LFU;
    }

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public void reset() {
        entries.clear();
        order.clear();
        tick = 0;
    }

    @Override
    public AccessOutcome access(int key) {
        tick++;
        CacheEntry existing = entries.get(key);
        if (existing != null) {
            entries.put(key, existing.withAccess(tick));
            return AccessOutcome.ofHit();
        }

        if (entries.size() < capacity) {
            entries.put(key, new CacheEntry(key, 1, tick));
            order.add(key);
            return AccessOutcome.ofMiss();
        }

        int victim = findLfuKey();
        entries.remove(victim);
        order.remove(Integer.valueOf(victim));
        entries.put(key, new CacheEntry(key, 1, tick));
        order.add(key);
        return AccessOutcome.ofMissEvict(victim);
    }

    private int findLfuKey() {
        int victim = order.get(0);
        CacheEntry best = entries.get(victim);
        for (int key : order) {
            CacheEntry e = entries.get(key);
            if (e.frequency() < best.frequency()
                    || (e.frequency() == best.frequency() && e.lastUsed() < best.lastUsed())) {
                best = e;
                victim = key;
            }
        }
        return victim;
    }

    @Override
    public List<Integer> snapshotSlots() {
        List<Integer> slots = new ArrayList<>(capacity);
        for (int key : order) {
            slots.add(key);
        }
        while (slots.size() < capacity) {
            slots.add(null);
        }
        return slots;
    }
}
