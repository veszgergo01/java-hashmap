package src.implementations;

import src.definitions.EntryState;
import src.hashing.HashStrategy;

public class LinearProbingHashMap<K, V> extends OurAbstractHashMap<K, V> {
    public LinearProbingHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LAMBDA_VALUE);
    }

    public LinearProbingHashMap(float loadFactor) {
        this(DEFAULT_CAPACITY, loadFactor);
    }

    public LinearProbingHashMap(HashStrategy hashStrategy) {
        super(hashStrategy);
    }

    public LinearProbingHashMap(int capacity, float loadFactor) {
        super(capacity, loadFactor);
    }

    @Override
    public boolean delete(K key) {
        int index = hash(key);

        if (!has(key)) return false;

        /** Guaranteed to halt, due to contract of {@code this#has()} */
        while (!key.equals(table[index].key)) {
            index = (index + 1) % capacity;
        }

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

        /** Guaranteed to halt, due to contract of {@code this#has()} */
        while (table[index] == null || !key.equals(table[index].key)) {
            index = (index + 1) % capacity;
        }

        return table[index].value;
    }

    @Override
    public boolean has(K key) {
        int index = hash(key);
        final int initIndex = index;

        while (table[index] == null || !key.equals(table[index].key)) {
            index = (index + 1) % capacity;
            if (index == initIndex) return false;
        }

        return true;
    }

    @Override
    protected int handleCollision(int index) {
        while (table[index] != null && EntryState.OCCUPIED.equals(table[index].state)) {
            index = (index + 1) % capacity;
        }

        return index;
    }
}
