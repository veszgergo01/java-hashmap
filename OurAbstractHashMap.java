/**
 * Abstract class for overarching variables and basic constructors.
 * Extending classes will add precise implementation details (open-addressing, linear probing, etc.)
 */
public abstract class OurAbstractHashMap<K, V> implements HashMapInterface<K, V> {
    public static final int DEFAULT_CAPACITY = 16;
    public static final float DEFAULT_LAMBDA_VALUE = 0.75f;

    protected float lambda;
    protected int capacity;
    protected Entry<K, V>[] table;

    protected V empty;
    protected V tombstone;

    public OurAbstractHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LAMBDA_VALUE);
    }

    public OurAbstractHashMap(int capacity, float loadFactor) {
        this.capacity = capacity;
        this.lambda = loadFactor;
        this.table = new Entry[capacity];

        for (int i = 0; i < table.length; i++) {
            table[i] = new Entry<K, V>(EntryState.EMPTY);
        }
    }

    /* TODO add method/constructor not to be used by user but for when the load-factor
     demands re-calibration */
}
