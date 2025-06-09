package src.implementations;

import src.definitions.EntryState;
import src.hashing.HashStrategy;
import src.hashing.Hasher;

public class DoubleHashingHashMap<K, V> extends OurAbstractHashMap<K, V> {

    public DoubleHashingHashMap() {
        super();
    }

    public DoubleHashingHashMap(float loadFactor) {
        super(loadFactor);
    }

    public DoubleHashingHashMap(HashStrategy hashStrategy) {
        super(hashStrategy);
    }

    private int eye = 1; // "i" in the slides, just didn't want to use i because it's a common index
    private Hasher<K> sh = new Hasher<>();

    // Ugly solution to solve OutOfMemoryException
    private K currentInsertKey;

    @Override
    public boolean insert(K key, V value) {
        this.currentInsertKey = key;
        return super.insert(key, value);
    }

    @Override
    public boolean delete(K key) {
        int index = hash(key);

        if (!has(key)) return false;

        while (table[index] == null || !key.equals(table[index].key)) {
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

        while (table[index] == null || !key.equals(table[index].key)) {
            index = calculateNewIndex(key, index);
            eye++;
            if (index == initIndex) return false;
        }
        eye = 1;

        return true;
    }

    @Override
    protected int handleCollision(int index) {
        int probes = 0;
        nrOfProbes++;

        while (table[index] != null && EntryState.OCCUPIED.equals(table[index].state)) {
            index = calculateNewIndex(currentInsertKey, index);
            eye++;
            probes++;

            nrOfProbes++;

            if (probes >= capacity) {
                resize();
                index = hash(currentInsertKey);
                eye = 1;
            }
        }

        eye = 1;
        return index;
    }


    private int calculateNewIndex(K key, int index) {
        int newIndex = (sh.hash(key, HashStrategy.JAVA_DEFAULT, capacity) + eye * sh.hash(key, HashStrategy.RELATIVE_PRIME, capacity)) % capacity;
        if (newIndex < 0) newIndex += capacity;
        return newIndex;
    }
}
