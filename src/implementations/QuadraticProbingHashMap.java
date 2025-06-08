package src.implementations;

import src.definitions.EntryState;
import src.hashing.HashStrategy;

public class QuadraticProbingHashMap<K, V> extends OurAbstractHashMap<K, V> {
    public QuadraticProbingHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LAMBDA_VALUE);
    }

    public QuadraticProbingHashMap(float loadFactor) {
        this(DEFAULT_CAPACITY, loadFactor);
    }

    public QuadraticProbingHashMap(HashStrategy hashStrategy) {
        super(hashStrategy);
    }

    public QuadraticProbingHashMap(int capacity, float loadFactor) {
        super(capacity, loadFactor);
    }

    @Override
    public boolean delete(K key) {
        if (!has(key)) return false;

        int baseIndex = hash(key);
        int index;

        for (int i = 0; i < capacity; i++) {
            index = (baseIndex + i * i) % capacity;

            if (table[index].state == EntryState.OCCUPIED && key.equals(table[index].key)) {
                table[index].key = null;
                table[index].value = null;
                table[index].state = EntryState.TOMBSTONE;
                size--;
                return true;
            }
        }

        // Shouldn't reach this
        return false; 
    }

    @Override
    public V get(K key) {
        if (!has(key)) return null;

        int baseIndex = hash(key);
        int index;

        for (int i = 0; i < capacity; i++) {
            index = (baseIndex + i * i) % capacity;

            if (table[index].state == EntryState.OCCUPIED && key.equals(table[index].key)) {
                return table[index].value;
            }
        }
       
        // Shouldn't reach this
        return null; 
    }

    @Override
    public boolean has(K key) {
        int baseIndex = hash(key);
        int index;

        for (int i = 0; i < capacity; i++) {
            index = (baseIndex + i * i) % capacity;

            if (table[index] == null) {
                return false; // Key can't be in table beyond this
            }

            if (table[index].state == EntryState.OCCUPIED && key.equals(table[index].key)) {
                return true;
            }

        }

        return false;
    }

    @Override
    protected int handleCollision(int initialIndex) {
        int baseIndex = initialIndex;
        int index;

        for (int i = 0; i < capacity; i++) {
            index = (baseIndex + i * i) % capacity;

            if (table[index]== null || table[index].state == EntryState.TOMBSTONE) {
                return index;
            }
        }

        resize();
        return handleCollision(initialIndex);
    }
}