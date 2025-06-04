package src.implementations;

import src.definitions.Entry;
import src.definitions.EntryState;
import src.hashing.HashStrategy;
import src.hashing.Hasher;

public class CuckooHashMap<K, V> extends OurAbstractHashMap<K, V> {

    // For Cuckoo hashing the load factor is best to be kept below 50%
    // Source: https://link.springer.com/chapter/10.1007/3-540-44676-1_10 TODO check
    // https://www.geeksforgeeks.org/cuckoo-hashing/ <-- definitely here
    public static final float DEFAULT_LAMBDA_VALUE = 0.5f;
    private final HashStrategy[] AVAILABLE_HASH_STRATEGIES = HashStrategy.values();

    /** Points to the next unused strategy in {@link AVAILABLE_HASH_STRATEGIES} */
    private int availableStrategiesIndex = 2; // The first two strategies are being used

    private Hasher<K> sh = new Hasher<>();
    private HashStrategy hashStrategy2;
    /** Prevents infinite loops. */ // TODO is there a more elegant solution? Maybe research better, dynamic numbers for this
    private static final int MAX_LOOP = 32;

    public CuckooHashMap() {
        this(DEFAULT_HASHING_STRATEGY, HashStrategy.values()[1]);
    }

    public CuckooHashMap(HashStrategy h1, HashStrategy h2) {
        this(DEFAULT_CAPACITY, DEFAULT_LAMBDA_VALUE, h1, h2);
    }

    public CuckooHashMap(int capacity, float lambda, HashStrategy h1, HashStrategy h2) {
        super(capacity, lambda);
        hashStrategy = h1;
        hashStrategy2 = h2;
    }

    @Override
    public boolean insert(K key, V value) {
        Entry<K, V> newEntry = new Entry<>(key, value);
        Entry<K, V> initialEntry = newEntry;
        int countLoop = 0;

        do {
            int index1 = sh.hash(key, hashStrategy, capacity);
            // Empty: no worries
            if (EntryState.OCCUPIED != table[index1].state) {
                table[index1] = newEntry;
                size++;
                if ((float) size / capacity >= lambda) resize();
                return false;
            }

            // Just updating the value
            if (table[index1].key.equals(key)) {
                table[index1].value = value;
                return true;
            }

            // Occupied: kick out inhibitant
            Entry<K, V> evictedEntry = table[index1];
            table[index1] = newEntry;

            // Now inserting evicted entry
            key = evictedEntry.key;
            value = evictedEntry.value;
            newEntry = evictedEntry;

            // No need to try the first place: just got evicted from there
            int index2 = sh.hash(key, hashStrategy2, capacity);
            if (EntryState.OCCUPIED != table[index2].state) {
                table[index2] = newEntry;
                size++;
                if ((float) size / capacity >= lambda) resize();
                return false;
            }

            // Evict again and continue looping
            evictedEntry = table[index2];
            table[index2] = newEntry;
            key = evictedEntry.key;
            value = evictedEntry.value;
            newEntry = evictedEntry;

            if (countLoop++ > MAX_LOOP) break;
        } while (!initialEntry.equals(newEntry));

        // If we get here, we needed to try too many times, so rehash and retry.
        resize(); // TODO could i simply use rehash here? i tried to but the stack kept overflowing...
        return insert(key, value);
    }

    @Override
    public boolean delete(K key) {
        int index1 = sh.hash(key, hashStrategy, capacity);
        int index2 = sh.hash(key, hashStrategy2, capacity);

        if (key.equals(table[index1].key)) {
            table[index1].key = null;
            table[index1].value = null;
            table[index1].state = EntryState.TOMBSTONE;
            return true;
        }

        if (key.equals(table[index2].key)) {
            table[index2].key = null;
            table[index2].value = null;
            table[index2].state = EntryState.TOMBSTONE;
            return true;
        }

        return false;
    }

    @Override
    public V get(K key) {
        if (!has(key)) return null;

        int index1 = sh.hash(key, hashStrategy, capacity);
        int index2 = sh.hash(key, hashStrategy2, capacity);

        if (key.equals(table[index1].key)) {
            return table[index1].value;
        } else {
            return table[index2].value;
        }
    }

    @Override
    public boolean has(K key) {
        int index1 = sh.hash(key, hashStrategy, capacity);
        int index2 = sh.hash(key, hashStrategy2, capacity);

        return key.equals(table[index1].key) || key.equals(table[index2].key);
    }

    @Override
    protected int handleCollision(int initialIndex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleCollision'");
    }

    @Override
    protected void rehash(Entry<K, V>[] oldTable) {
        // Switch strategies
        hashStrategy = AVAILABLE_HASH_STRATEGIES[availableStrategiesIndex % AVAILABLE_HASH_STRATEGIES.length];
        hashStrategy2 = AVAILABLE_HASH_STRATEGIES[(availableStrategiesIndex + 1) % AVAILABLE_HASH_STRATEGIES.length];
        
        availableStrategiesIndex = (availableStrategiesIndex + 2) % AVAILABLE_HASH_STRATEGIES.length;

        super.rehash(oldTable);
    }
}
