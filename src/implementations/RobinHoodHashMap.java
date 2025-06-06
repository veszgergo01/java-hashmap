package src.implementations;
import src.definitions.EntryState;
import src.hashing.HashStrategy;

import src.definitions.Entry;

public class RobinHoodHashMap<K, V> extends LinearProbingHashMap<K, V> {
    public RobinHoodHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LAMBDA_VALUE);
    }

    public RobinHoodHashMap(float loadFactor) {
        this(DEFAULT_CAPACITY, loadFactor);
    }

    public RobinHoodHashMap(HashStrategy hashStrategy) {
        super(hashStrategy);
    }

    public RobinHoodHashMap(int capacity, float loadFactor) {
        super(capacity, loadFactor);
    }

    @Override
    public boolean insert(K key, V value) {
        int index = hash(key);
        int i = 0;

        Entry<K, V> current = new Entry<>(key, value);

        while (true) {
            int probeIndex = (index + i) % capacity;

            // EMPTY or TOMBSTONE: insert here
            if (table[probeIndex].state != EntryState.OCCUPIED) {
                table[probeIndex] = current;
                size++;
                if ((float) size / capacity >= lambda) resize();
                return false; // newly inserted
            }

            // Key already exists: update
            if (key.equals(table[probeIndex].key)) {
                table[probeIndex].value = value;
                return true;
            }

            // Calculate probe distances
            int existingHash = hash(table[probeIndex].key);
            int existingProbeDist = (probeIndex - existingHash + capacity) % capacity;
            int currentProbeDist = i;

            // Robin Hood logic: swap if current probe distance > existing
            if (currentProbeDist > existingProbeDist) {
                // Swap
                Entry<K, V> temp = table[probeIndex];
                table[probeIndex] = current;
                current = temp;
                index = existingHash;
                i = existingProbeDist; // resume probing with displaced item
            }

            i++;

            if (i >= capacity) {
                throw new RuntimeException("Hash table is full or probe limit exceeded");
            }
        }
    }

    @Override
    public boolean delete(K key) {
        int index = hash(key);
        int i = 0;

        while (i < capacity) {
            int probeIndex = (index + i) % capacity;

            // Stop if we hit an empty slot — key not in table
            if (table[probeIndex].state == EntryState.EMPTY) {
                return false;
            }

            // Found the key to delete
            if (table[probeIndex].state == EntryState.OCCUPIED && key.equals(table[probeIndex].key)) {
                // Remove the entry
                table[probeIndex].key = null;
                table[probeIndex].value = null;
                table[probeIndex].state = EntryState.TOMBSTONE;

                size--;

                // Shift-back subsequent entries to fill the hole
                int j = (probeIndex + 1) % capacity;

                while (table[j].state == EntryState.OCCUPIED) {
                    int home = hash(table[j].key);
                    int distFromHome = (j - home + capacity) % capacity;

                    if (distFromHome > 0) {
                        // Shift entry back
                        table[probeIndex] = table[j];
                        table[j] = new Entry<>();
                        probeIndex = j;
                    } else {
                        break;
                    }
                    
                    j = (j + 1) % capacity;
                }

                return true;
            }

            i++;
        }

        return false;
    }
}