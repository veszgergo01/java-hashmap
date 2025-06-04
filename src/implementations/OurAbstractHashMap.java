package src.implementations;
import java.util.Iterator;
import java.util.NoSuchElementException;

import src.hashing.HashStrategy;
import src.hashing.StringHasher;

import src.definitions.OurMapInterface;

import src.definitions.Entry;
import src.definitions.EntryState;

/**
 * Abstract class for overarching variables, methods and basic constructors.
 * Extending classes will add precise implementation details (e.g. hashing strategies).
 */
public abstract class OurAbstractHashMap<K, V> implements OurMapInterface<K, V> {
    public static final int DEFAULT_CAPACITY = 5;
    public static final float DEFAULT_LAMBDA_VALUE = 0.75f;
    public static final HashStrategy DEFAULT_HASHING_STRATEGY = HashStrategy.values()[0];

    protected float lambda;
    protected int capacity;
    protected HashStrategy hashStrategy;
    protected Entry<K, V>[] table;

    protected int size;

    public OurAbstractHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LAMBDA_VALUE, DEFAULT_HASHING_STRATEGY);
    }

    public OurAbstractHashMap(int capacity) {
        this(capacity, DEFAULT_LAMBDA_VALUE, DEFAULT_HASHING_STRATEGY);
    }

    public OurAbstractHashMap(float lambda) {
        this(DEFAULT_CAPACITY, lambda, DEFAULT_HASHING_STRATEGY);
    }

    public OurAbstractHashMap(int capacity, float lambda) {
        this(capacity, lambda, DEFAULT_HASHING_STRATEGY);
    }

    public OurAbstractHashMap(HashStrategy hashStrategy) {
        this(DEFAULT_CAPACITY, DEFAULT_LAMBDA_VALUE, hashStrategy);
    }

    /**
     * Instatiates an empty hashmap.
     * 
     * @param capacity the number of buckets (capacity of the hash table)
     * @param lambda the load factor for balancing
     * @param hashStrategy the hashing strategy to be used (see {@link HashStrategy})
     */
    public OurAbstractHashMap(int capacity, float lambda, HashStrategy hashStrategy) {
        this.capacity = capacity;
        this.lambda = lambda;
        this.hashStrategy = hashStrategy;
        this.table = new Entry[capacity];

        for (int i = 0; i < table.length; i++) {
            table[i] = new Entry<K, V>(EntryState.EMPTY);
        }

        this.size = 0;
    }

    @Override
    public boolean insert(K key, V value) {
        // First step needed to determine if key was already in there
        int index = hash(key);
        boolean collision = !key.equals(table[index].key);
        
        if (collision) {
            index = handleCollision(index);
        }
        
        table[index].key = key;
        table[index].value = value;
        table[index].state = EntryState.OCCUPIED;
        size++;
        if ((float) size / capacity >= lambda) resize();

        return collision;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean clear() {
        boolean result = !isEmpty();

        for (int i = 0; i < table.length; i++) {
            table[i] = new Entry<K, V>(EntryState.EMPTY);
        }

        size = 0;

        return result;
    }

    @Override
    public Iterator<Entry<K, V>> items() {
        return new Iterator<Entry<K, V>>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                while (index < table.length && table[index].key == null) {
                    index++;
                }
                return index < table.length;
            }

            @Override
            public Entry<K, V> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                // skips to the next element immediately to avoid infinite loop in hasNext
                return table[index++];
            }
        };
    }

    @Override
    public int hash(K key) {
        return new StringHasher<K>().hash(key, hashStrategy, capacity);
    }

    @Override
    public int resize() {
        Entry<K, V>[] oldTable = table;

        // Initialize new table
        capacity = capacity * 2;
        table = new Entry[capacity];
        for (int i = 0; i < table.length; i++) {
            table[i] = new Entry<K, V>(EntryState.EMPTY);
        }

        rehash(oldTable);

        return capacity;
    }

    /**
     * Handles hash collisions. Classes extending {@code this} must implement
     * this method according to their hashing strategy.
     * 
     * @param initialIndex where the hash function would put the key
     * @return the smallest {@code int i} that is greater or equal to {@code initialIndex} and
     *         {@code table[i].state != EntryState.OCCUPIED}
     */
    protected abstract int handleCollision(int initialIndex);

    /**
     * Reconstructs the hash table. The method may be called when the table is
     * resized or when the hash function changes.
     * 
     * @modifies {@code this.table} 
     * 
     * @param oldTable the original state of the table
     */
    protected void rehash(Entry<K, V>[] oldTable) {
        size = 0;

        for (Entry<K, V> entry : oldTable) {
            if (EntryState.OCCUPIED.equals(entry.state)) {
                insert(entry.key, entry.value);
            }
        }
    }
}
