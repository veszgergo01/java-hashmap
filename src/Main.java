package src;

import src.definitions.Entry;
import src.definitions.OurMapInterface;
import src.implementations.QuadraticProbingHashMap;
import src.implementations.RobinHoodHashMap;

public class Main {
    public static void main(String[] args) {
        OurMapInterface<String, Integer> map = new RobinHoodHashMap<>(8, 0.75f);

        // System.out.println("=== INSERT TESTS ===");
        // map.insert("A", 1);
        // map.insert("B", 2);
        // map.insert("C", 3);
        // map.insert("A", 100);  // update A

        // System.out.println("A = " + map.get("A")); // expect 100
        // System.out.println("B = " + map.get("B")); // expect 2
        // System.out.println("Z = " + map.get("Z")); // expect null
        // System.out.println("Has C? " + map.has("C")); // expect true
        // System.out.println("Has Z? " + map.has("Z")); // expect false

        // System.out.println("\n=== DELETE TESTS ===");
        // System.out.println("Deleting B...");
        // map.delete("B");
        // System.out.println("Has B? " + map.has("B")); // expect false
        // System.out.println("B = " + map.get("B"));     // expect null

        // System.out.println("\n=== COLLISION TESTS ===");
        // map.insert("X", 9);
        // map.insert("Y", 10);
        // map.insert("Z", 11);
        // map.insert("W", 12);  // likely to cause quadratic probing

        // System.out.println("X = " + map.get("X"));
        // System.out.println("Y = " + map.get("Y"));
        // System.out.println("Z = " + map.get("Z"));
        // System.out.println("W = " + map.get("W"));

        // System.out.println("\nUpdating X...");
        // map.insert("X", 99);
        // System.out.println("X = " + map.get("X")); // expect 99

        map.insert("Aa", 1);
        map.insert("BB", 2);
        map.insert("A", 1);
        int size = map.size();
        System.out.println("Size: " + size);
        map.insert("B", 1);
        map.insert("AaAa", 3);
        map.insert("C", 1);
        map.insert("AaAa", 7);

        
        System.out.println("\n=== FINAL MAP CONTENTS ===");
        for (Entry<String, Integer> entry : (Iterable<Entry<String, Integer>>) map::items) {
            System.out.println(entry.key + " -> " + entry.value);
        }
        size = map.size();
        System.out.println("Size: " + size);
        System.out.println("All output above should match expected values.");
        map.clear();
        size = map.size();
        System.out.println("Size: " + size);
        boolean empty = map.isEmpty();
        System.out.println("Empty?: " + empty);
        System.out.println("\n=== FINAL MAP CONTENTS ===");
        for (Entry<String, Integer> entry : (Iterable<Entry<String, Integer>>) map::items) {
            System.out.println(entry.key + " -> " + entry.value);
        }
    }
}
