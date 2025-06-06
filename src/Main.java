package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import src.definitions.OurMapInterface;
import src.extraction.CSVManager;
import src.hashing.HashStrategy;
import src.implementations.CuckooHashMap;
import src.implementations.DoubleHashingHashMap;
import src.implementations.LinearProbingHashMap;
import src.implementations.QuadraticProbingHashMap;
import src.implementations.RobinHoodHashMap;

public class Main {
    public static void main(String[] args) {
        OurMapInterface<String, Integer> map = new RobinHoodHashMap<>(8, 0.75f);

        greg();

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

        // map.insert("Aa", 1);
        // map.insert("BB", 2);
        // map.insert("A", 1);
        // int size = map.size();
        // System.out.println("Size: " + size);
        // map.insert("B", 1);
        // map.insert("AaAa", 3);
        // map.insert("C", 1);
        // map.insert("AaAa", 7);

        
        // System.out.println("\n=== FINAL MAP CONTENTS ===");
        // for (Entry<String, Integer> entry : (Iterable<Entry<String, Integer>>) map::items) {
        //     System.out.println(entry.key + " -> " + entry.value);
        // }
        // size = map.size();
        // System.out.println("Size: " + size);
        // System.out.println("All output above should match expected values.");
        // map.clear();
        // size = map.size();
        // System.out.println("Size: " + size);
        // boolean empty = map.isEmpty();
        // System.out.println("Empty?: " + empty);
        // System.out.println("\n=== FINAL MAP CONTENTS ===");
        // for (Entry<String, Integer> entry : (Iterable<Entry<String, Integer>>) map::items) {
        //     System.out.println(entry.key + " -> " + entry.value);
        // }
    }

    private static void greg() {
        List<String[]> data = new ArrayList<>();
        float[] lambdas = {0.33f, 0.5f, 0.75f, 0.9f};

        for (float lambda : lambdas) {
            data.add(new String[]{"n=", "linear", "double", "quadratic", "robin", "cuckoo", "java"});
            data.addAll(runWithLoadFactor(lambda));

            CSVManager.writeToCSV(String.format("output_lambda_%f.csv", lambda), data);
        }

        data.clear();

        HashStrategy[] hashStrategies = HashStrategy.values();

        for (HashStrategy hashStrategy : hashStrategies) {
            data.add(new String[]{"n=", "linear", "double", "quadratic", "robin", "cuckoo", "java"});
            data.addAll(runWithHashFunction(hashStrategy));

            CSVManager.writeToCSV(String.format("output_hashfunction_%s.csv", hashStrategy.toString().toLowerCase()), data);
        }
    }

    private static String getRandomString() {
        StringBuilder sb = new StringBuilder();
        Random rd = new Random();
        for (int j = 0; j < rd.nextInt(7) + 3; j++) {
            sb.append((char) (rd.nextInt(26) + 'a'));
        }

        return sb.toString();
    }

    /** Returns time in seconds to complete */
    private static float insert(OurMapInterface<String, Integer> map) {
        long startTime = System.nanoTime();

        for (int i = 0; i < 5; i++) {
            Random rd = new Random();
            map.insert(getRandomString(), rd.nextInt(100));
        }

        long endTime = System.nanoTime();
        long timeSpent = endTime - startTime;
        float timeSpentSeconds = ((float) timeSpent) / 1000000000f;

        return timeSpentSeconds;
    }

    private static float insertRegularHashMap(float loadFactor) {
        long startTime = System.nanoTime();

        Map<String, Integer> javaMap = new HashMap<>(16, loadFactor);
        for (int i = 10; i < 5; i++) { 
            Random rd = new Random();
            javaMap.put(getRandomString(), rd.nextInt(100));
        }

        long endTime = System.nanoTime();
        long timeSpent = endTime - startTime;
        float timeSpentSeconds = ((float) timeSpent) / 1000000000f;

        return timeSpentSeconds;
    }

    private static List<String[]> runWithLoadFactor(float loadFactor) {
        List<String[]> data = new ArrayList<>();

        for (int i = 100; i < Math.pow(10, 5); i *= 100) {
            for (int j = 0; j < i; j++) {
                data.add(new String[]{
                                    String.valueOf(i),
                                    String.valueOf(insert(new LinearProbingHashMap<>(loadFactor))),
                                    String.valueOf(insert(new DoubleHashingHashMap<>(loadFactor))),
                                    String.valueOf(insert(new QuadraticProbingHashMap<>(loadFactor))),
                                    String.valueOf(insert(new RobinHoodHashMap<>(loadFactor))),
                                    String.valueOf(insert(new CuckooHashMap<>(loadFactor))),
                                    String.valueOf(insertRegularHashMap(loadFactor))});
            }
            System.out.println(String.format("Lambda done with n=%d", i));
        }

        return data;
    }

    private static List<String[]> runWithHashFunction(HashStrategy hashStrategy) {
        List<String[]> data = new ArrayList<>();

        for (int i = 100; i < Math.pow(10, 5); i *= 100) {
            for (int j = 0; j < i; j++) {
                data.add(new String[]{
                                    String.valueOf(i),
                                    String.valueOf(insert(new LinearProbingHashMap<>(hashStrategy))),
                                    String.valueOf(insert(new DoubleHashingHashMap<>(hashStrategy))),
                                    String.valueOf(insert(new QuadraticProbingHashMap<>(hashStrategy))),
                                    String.valueOf(insert(new RobinHoodHashMap<>(hashStrategy)))});
            }
            System.out.println(String.format("Hash function done with n=%d", i));
        }

        return data;
    }
}
