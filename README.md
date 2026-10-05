# Cache Simulator (LRU / LFU)

A **Java console** app for exploring **cache replacement policies** on a **fully associative** cache with fixed capacity. Access a sequence of integer keys, watch hits and misses, and see which key is evicted under **LRU** or **LFU**. No external libraries.

An interactive **browser demo** (TypeScript / Next.js) is available on the live portfolio site: [rjedwards.dev/projects/cache-simulator](https://rjedwards.dev/projects/cache-simulator).

## Policies

| Policy | Evicts | Notes |
|--------|--------|--------|
| **LRU** | Least recently used key | Recency-based; ties do not arise for distinct ticks |
| **LFU** | Least frequently used key | Frequency-based; ties broken by least recently used |

## Presets

Three built-in access traces (shared with the portfolio demo):

1. **Hot-cold** — `1 2 3 1 4 1 5` (classic LRU teaching trace)
2. **Frequency skew** — `1 1 1 2 2 3 4` (LFU tends to keep hot `1`)
3. **Thrash** — `1 2 3 4 1 2 3 4 1` (cycling beyond capacity)

Legend in step mode: `_` empty slot, numbers are cached keys.

## Requirements

- [JDK](https://openjdk.org/) 17 or newer

## Compile

From the repository root:

```bash
javac -d out -sourcepath src/main/java src/main/java/cache/app/CacheSimulatorApp.java
```

## Run

```bash
java -cp out cache.app.CacheSimulatorApp
```

Follow the prompts to pick a policy, capacity, and access trace.

## Project layout

```
src/main/java/cache/
  model/   Policy, entries, steps, presets
  core/    LRU and LFU simulators
  app/     Console UI
```
