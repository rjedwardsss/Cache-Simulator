package cache.model;

public record CacheEntry(int key, int frequency, long lastUsed) {
    public CacheEntry withAccess(long tick) {
        return new CacheEntry(key, frequency + 1, tick);
    }
}
