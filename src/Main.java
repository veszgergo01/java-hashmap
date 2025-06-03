package src;
import java.util.Iterator;

import javax.sound.sampled.Line;

import src.definitions.Entry;
import src.definitions.OurMapInterface;
import src.implementations.CuckooHashMap;
import src.implementations.DoubleHashingHashMap;
import src.implementations.LinearProbingHashMap;
import src.implementations.QuadraticProbingHashMap;

class Main {
    public static void main(String[] args) {
        OurMapInterface<String, Integer> map = new CuckooHashMap<>();

        for (int i = 0; i < 22; i++) {
            map.insert(String.format("%d", i), i);
        }

        map.insert("FB", 2);
        map.insert("Ea", 4);

        // map.delete("5");

        // Iterator<Entry<String, Integer>> it = map.items();
        // while (it.hasNext()) {
        //     Entry<String, Integer> entry = it.next();
        //     System.out.println(String.format("%s: %s", entry.key, entry.value));
        // }

        System.out.println("finito");
    }
}