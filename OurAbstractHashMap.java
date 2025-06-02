import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Abstract class for overarching variables, methods and basic constructors.
 * Extending classes will add precise implementation details (e.g. hashing strategies).
 */
public abstract class OurAbstractHashMap<K, V> implements HashMapInterface<K, V> {
    public static final int DEFAULT_CAPACITY = 16;
    public static final float DEFAULT_LAMBDA_VALUE = 0.75f;

    protected float lambda;
    protected int capacity;
    protected Entry<K, V>[] table;

    protected int size;

    public OurAbstractHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LAMBDA_VALUE);
    }

    /**
     * Instatiates an empty hashmap.
     * 
     * @param capacity the number of buckets (capacity of the hash table)
     * @param lambda the load factor for balancing
     */
    public OurAbstractHashMap(int capacity, float lambda) {
        this.capacity = capacity;
        this.lambda = lambda;
        this.table = new Entry[capacity];

        for (int i = 0; i < table.length; i++) {
            table[i] = new Entry<K, V>(EntryState.EMPTY);
        }

        this.size = 0;
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
        int result = key.hashCode() % capacity;
        if (result < 0) {
            result += capacity;
        }

        return result;
    }

    @Override
    public int resize() {
        int newCapacity = capacity * 2;
        Entry<K, V>[] newTable = new Entry[newCapacity];
        for (int i = 0; i < newTable.length; i++) {
            newTable[i] = new Entry<>(EntryState.EMPTY);
        }

        for (int i = 0; i < table.length; i++) {
            newTable[i] = table[i];
        }

        this.table = newTable;
        this.capacity = newCapacity;

        return newCapacity;
    }
}
