package cache.app;

import cache.core.CacheSimulator;
import cache.core.LfuCache;
import cache.core.LruCache;
import cache.model.Policy;
import cache.model.SimStep;
import cache.model.SimulationResult;
import cache.model.TracePresets;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public final class CacheSimulatorApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Cache Simulator (LRU / LFU)");
        System.out.println("Fully associative cache with fixed capacity.");
        System.out.println();

        Policy policy = choosePolicy(scanner);
        int capacity = chooseCapacity(scanner);
        TracePresets.Id presetId = choosePreset(scanner);
        TracePresets.Preset preset = TracePresets.get(presetId);

        CacheSimulator sim = policy == Policy.LRU
                ? new LruCache(capacity)
                : new LfuCache(capacity);

        System.out.println("\nPolicy: " + policy.label());
        System.out.println("Capacity: " + capacity);
        System.out.println("Trace (" + preset.name() + "): " + formatKeys(preset.keys()));

        SimulationResult result = sim.runTrace(preset.keys());
        printSummary(result);

        if (askYesNo(scanner, "\nStep through simulation? (y/n): ")) {
            stepThrough(result, capacity);
        } else {
            printAllSteps(result, capacity);
        }

        scanner.close();
    }

    private static Policy choosePolicy(Scanner scanner) {
        Policy[] values = Policy.values();
        System.out.println("Choose a policy:");
        for (int i = 0; i < values.length; i++) {
            System.out.printf("  %d) %s%n", i + 1, values[i].label());
        }
        while (true) {
            System.out.print("Enter 1-" + values.length + ": ");
            String line = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(line);
                if (choice >= 1 && choice <= values.length) {
                    return values[choice - 1];
                }
            } catch (NumberFormatException ignored) {
                // reprompt
            }
            System.out.println("Invalid choice.");
        }
    }

    private static int chooseCapacity(Scanner scanner) {
        System.out.println("\nChoose capacity:");
        System.out.println("  1) 3");
        System.out.println("  2) 4");
        System.out.println("  3) 5");
        while (true) {
            System.out.print("Enter 1-3: ");
            String line = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(line);
                if (choice == 1) return 3;
                if (choice == 2) return 4;
                if (choice == 3) return 5;
            } catch (NumberFormatException ignored) {
                // reprompt
            }
            System.out.println("Invalid choice.");
        }
    }

    private static TracePresets.Id choosePreset(Scanner scanner) {
        TracePresets.Id[] ids = TracePresets.ids();
        System.out.println("\nChoose an access trace:");
        for (int i = 0; i < ids.length; i++) {
            TracePresets.Preset p = TracePresets.get(ids[i]);
            System.out.printf("  %d) %s — %s%n", i + 1, p.name(), formatKeys(p.keys()));
        }
        while (true) {
            System.out.print("Enter 1-" + ids.length + ": ");
            String line = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(line);
                if (choice >= 1 && choice <= ids.length) {
                    return ids[choice - 1];
                }
            } catch (NumberFormatException ignored) {
                // reprompt
            }
            System.out.println("Invalid choice.");
        }
    }

    private static void printSummary(SimulationResult result) {
        System.out.println("\n--- Results ---");
        System.out.println("Hits: " + result.hits());
        System.out.println("Misses: " + result.misses());
        System.out.printf("Hit rate: %.1f%%%n", result.hitRate() * 100.0);
    }

    private static void printAllSteps(SimulationResult result, int capacity) {
        System.out.println();
        for (int i = 0; i < result.steps().size(); i++) {
            printStep(i + 1, result.steps().get(i), capacity);
        }
    }

    private static void stepThrough(SimulationResult result, int capacity) {
        Scanner scanner = new Scanner(System.in);
        List<SimStep> steps = result.steps();
        for (int i = 0; i < steps.size(); i++) {
            printStep(i + 1, steps.get(i), capacity);
            if (i < steps.size() - 1) {
                System.out.print("Press Enter for next step...");
                scanner.nextLine();
            }
        }
    }

    private static void printStep(int index, SimStep step, int capacity) {
        String outcome = step.hit() ? "HIT" : "MISS";
        String evict = step.evicted() == null ? "" : " (evict " + step.evicted() + ")";
        System.out.printf(
                "Step %d: access %d → %s%s | slots %s | hits=%d misses=%d%n",
                index,
                step.key(),
                outcome,
                evict,
                renderSlots(step.slots(), capacity),
                step.hits(),
                step.misses());
    }

    private static String renderSlots(List<Integer> slots, int capacity) {
        StringBuilder sb = new StringBuilder("[ ");
        for (int i = 0; i < capacity; i++) {
            if (i > 0) sb.append(" | ");
            Integer v = i < slots.size() ? slots.get(i) : null;
            sb.append(v == null ? "_" : v);
        }
        sb.append(" ]");
        return sb.toString();
    }

    private static String formatKeys(List<Integer> keys) {
        return keys.stream().map(String::valueOf).collect(Collectors.joining(" "));
    }

    private static boolean askYesNo(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim().toLowerCase();
            if (line.startsWith("y")) return true;
            if (line.startsWith("n")) return false;
            System.out.println("Please enter y or n.");
        }
    }
}
