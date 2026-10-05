package cache.model;

public enum Policy {
    LRU("Least Recently Used (LRU)"),
    LFU("Least Frequently Used (LFU)");

    private final String label;

    Policy(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
