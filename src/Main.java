package src;

import java.util.ArrayList;
import java.util.Arrays;
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
    private static final int RUN_TIMES = 5;
    private static final String[] HEADER = new String[]{"n=",   "linear_insertion_time", "linear_probe_length", "linear_rehash_count",
                                                                "double_insertion_time", "double_probe_length", "double_rehash_count",
                                                                "quadratic_insertion_time", "quadratic_probe_length", "quadratic_rehash_count",
                                                                "robin_insertion_time", "robin_probe_length", "robin_rehash_count",
                                                                "cuckoo_insertion_time", "cuckoo_probe_length", "cuckoo_rehash_count",
                                                                "java_insertion_time", "java_probe_length", "java_rehash_count"
                                                            };
    private static final float[] lambdas = {0.33f, 0.5f, 0.75f, 0.9f};
    private static final int MAX_INSERT_ELEMENTS = (int) Math.pow(10, 7);

    public static void main(String[] args) {
        List<String[]> data = new ArrayList<>();

        for (float lambda : lambdas) {
            data.add(HEADER);

            for (int i = 100; i < MAX_INSERT_ELEMENTS; i *= 10) {
                List<String> resultsList = new ArrayList<>();
                resultsList.add(String.format("%d", i));
                float[] result;

                result = insert(new LinearProbingHashMap<>(lambda), i);
                resultsList.addAll(List.of(String.valueOf(result[0]), String.valueOf(result[1]), String.valueOf(result[2])));

                result = insert(new DoubleHashingHashMap<>(lambda), i);
                resultsList.addAll(List.of(String.valueOf(result[0]), String.valueOf(result[1]), String.valueOf(result[2])));

                result = insert(new QuadraticProbingHashMap<>(lambda), i);
                resultsList.addAll(List.of(String.valueOf(result[0]), String.valueOf(result[1]), String.valueOf(result[2])));
                result = insert(new RobinHoodHashMap<>(lambda), i);
                resultsList.addAll(List.of(String.valueOf(result[0]), String.valueOf(result[1]), String.valueOf(result[2])));

                result = insert(new CuckooHashMap<>(lambda), i);
                resultsList.addAll(List.of(String.valueOf(result[0]), String.valueOf(result[1]), String.valueOf(result[2])));

                resultsList.addAll(List.of(String.valueOf(insertRegularHashMap(lambda, i)), "NA", "NA"));

                data.add(resultsList.toArray(new String[0]));
            }

            CSVManager.writeToCSV(String.format("output_lambda_%f.csv", lambda), data);
            data.clear();
        }

        HashStrategy[] hashStrategies = HashStrategy.values();

        for (HashStrategy hashStrategy : hashStrategies) {
            data.add(HEADER);
            for (int i = 100; i < MAX_INSERT_ELEMENTS; i *= 10) {
                List<String> resultsList = new ArrayList<>();
                resultsList.add(String.format("%d", i));
                float[] result;

                result = insert(new LinearProbingHashMap<>(hashStrategy), i);
                resultsList.addAll(List.of(String.valueOf(result[0]), String.valueOf(result[1]), String.valueOf(result[2])));

                result = insert(new DoubleHashingHashMap<>(hashStrategy), i);
                resultsList.addAll(List.of(String.valueOf(result[0]), String.valueOf(result[1]), String.valueOf(result[2])));

                result = insert(new QuadraticProbingHashMap<>(hashStrategy), i);
                resultsList.addAll(List.of(String.valueOf(result[0]), String.valueOf(result[1]), String.valueOf(result[2])));

                result = insert(new RobinHoodHashMap<>(hashStrategy), i);
                resultsList.addAll(List.of(String.valueOf(result[0]), String.valueOf(result[1]), String.valueOf(result[2])));

                // Cannot change strategies for Cuckoo, as it is being switched around
                resultsList.addAll(List.of("NA", "NA", "NA"));

                resultsList.addAll(List.of("NA", "NA", "NA"));

                data.add(resultsList.toArray(new String[0]));
            }

            CSVManager.writeToCSV(String.format("output_hashfunction_%s.csv", hashStrategy.toString().toLowerCase()), data);
            data.clear();
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
    private static float[] insert(OurMapInterface<String, Integer> map, int cnt) {
        System.out.println(String.format("Inserting %d elements using %s", cnt, map.getClass().getSimpleName()));

        float cumulativeTimeSeconds = 0;
        long cumulativeNrOfProbes = 0;
        long cumulativeNrOfRehashes = 0;

        for (int run = 0; run < RUN_TIMES; run++) {
            long startTime = System.nanoTime();

            for (int i = 0; i < cnt; i++) {
                Random rd = new Random();
                String key = getRandomString();
                int value = rd.nextInt(100);
                map.insert(key, value);
            }

            long endTime = System.nanoTime();

            long timeSpent = endTime - startTime;
            float timeSpentSeconds = ((float) timeSpent) / 1000000000f;
            cumulativeTimeSeconds += timeSpentSeconds;
            cumulativeNrOfProbes += map.getNumberOfProbes();
            cumulativeNrOfRehashes += map.getRehashCount();
            map.clear();
        }

        float averageTimeSeconds = cumulativeTimeSeconds / (float) RUN_TIMES;
        float averageNrOfProbes = (float) cumulativeNrOfProbes / (float) RUN_TIMES;
        float averageNrOfRehashes = (float) cumulativeNrOfRehashes / (float) RUN_TIMES;

        return new float[]{averageTimeSeconds, averageNrOfProbes, averageNrOfRehashes};
    }

    private static float insertRegularHashMap(float loadFactor, int times) {
        long startTime = System.nanoTime();

        Map<String, Integer> javaMap = new HashMap<>(16, loadFactor);
        for (int i = 10; i < times; i++) {
            Random rd = new Random();
            javaMap.put(getRandomString(), rd.nextInt(100));
        }

        long endTime = System.nanoTime();
        long timeSpent = endTime - startTime;
        float timeSpentSeconds = ((float) timeSpent) / 1000000000f;

        return timeSpentSeconds;
    }
}
