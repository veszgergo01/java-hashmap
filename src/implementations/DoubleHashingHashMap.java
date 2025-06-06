package src.implementations;

import src.definitions.EntryState;
import src.hashing.HashStrategy;
import src.hashing.Hasher;

public class DoubleHashingHashMap<K, V> extends OurAbstractHashMap<K, V> {

    public DoubleHashingHashMap(float loadFactor) {
        super(loadFactor);
    }

    public DoubleHashingHashMap(HashStrategy hashStrategy) {
        super(hashStrategy);
    }

    private int eye = 1; // "i" in the slides, just didn't want to use i because it's a common index
    private Hasher<K> sh = new Hasher<>();

    @Override
    public boolean delete(K key) {
        int index = hash(key);

        if (!has(key)) return false;

        while (!key.equals(table[index].key)) {
            index = calculateNewIndex(key, index);
            eye++;
        }
        eye = 1;

        table[index].key = null;
        table[index].value = null;
        table[index].state = EntryState.TOMBSTONE;
        size--;
        return true;
    }

    @Override
    public V get(K key) {
        int index = hash(key);

        if (!has(key)) return null;

        while (!key.equals(table[index].key)) {
            index = calculateNewIndex(key, index);
            eye++;
        }
        eye = 1;

        return table[index].value;
    }

    @Override
    public boolean has(K key) {
        int index = hash(key);
        final int initIndex = index;

        while (!key.equals(table[index].key)) {
            index = calculateNewIndex(key, index);
            eye++;
            if (index == initIndex) return false;
        }
        eye = 1;

        return true;
    }

    @Override
    protected int handleCollision(int index) {
        int initialIndex = index;
        while (EntryState.OCCUPIED.equals(table[index].state)) {
            index = calculateNewIndex(table[index].key, index);
            eye++;
            // If we have generated all possible indices in the ring, we will not
            // be able to resolve the hash collision within this set, so we need
            // to expand it
            if (initialIndex == index) resize();
        }
        eye = 1;

        return index;
    }

    private int calculateNewIndex(K key, int index) {
        return (sh.hash(key, HashStrategy.JAVA_DEFAULT, capacity) + eye * sh.hash(key, HashStrategy.STRING_FOLDING, capacity)) % capacity;
    }
}
