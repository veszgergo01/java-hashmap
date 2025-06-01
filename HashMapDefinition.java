import java.util.Iterator;

public abstract class HashMapDefinition<K, V> {
    public static final int DEFAULT_CAPACITY = 16;
    public static final float DEFAULT_LAMBDA_VALUE = 0.75f;

    protected float lambda;
    protected int capacity;
    protected Entry<K, V>[] table;

    public HashMapDefinition() {
        this(DEFAULT_CAPACITY, DEFAULT_LAMBDA_VALUE);
    }

    public HashMapDefinition(int capacity, float loadFactor) {
        this.capacity = capacity;
        this.lambda = loadFactor;
        this.table = new Entry[capacity];
    }

    public abstract boolean insert(K key, V value);

    public abstract boolean delete(K key);

    public abstract V get(K key);

    public abstract boolean has(K key);

    public abstract int size();

    public abstract boolean isEmpty();

    public abstract boolean clear();

    public abstract Iterator<Entry<K, V>> items();

    protected abstract int hash(K key);
}
