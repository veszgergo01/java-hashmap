package src.implementations;

import src.definitions.Entry;
import src.definitions.EntryState;
import src.hashing.HashStrategy;
import src.hashing.StringHasher;

public class CuckooHashMap<K, V> extends OurAbstractHashMap<K, V> {

    // For Cuckoo hashing the load factor is best to be kept below 50%
    // Source: https://link.springer.com/chapter/10.1007/3-540-44676-1_10 TODO check
    // https://www.geeksforgeeks.org/cuckoo-hashing/ <-- definitely here
    public static final float DEFAULT_LAMBDA_VALUE = 0.5f;
    private final HashStrategy[] AVAILABLE_HASH_STRATEGIES = HashStrategy.values();

    /** Points to the next unused strategy in {@link AVAILABLE_HASH_STRATEGIES} */
    private int availableStrategiesIndex = 2; // The first two strategies are being used

    private StringHasher<K> sh = new StringHasher<>();
    private HashStrategy hashStrategy2;

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

        for (int i = 0; i < capacity; i++) {
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
                size++;
                if ((float) size / capacity >= lambda) resize();
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
        }

        // If we get here, we needed to try too many times, so rehash and retry.
        rehash(table);
        return insert(key, value);
    }

    @Override
    public boolean delete(K key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public V get(K key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public boolean has(K key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'has'");
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
