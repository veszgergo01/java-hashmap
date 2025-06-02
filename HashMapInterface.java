import java.util.Iterator;

/**
 * Method headers definition.
 */
public interface HashMapInterface<K, V> {
    /**
     * Inserts {@code value} under the hash of {@code key}.
     * Returns true if the key was already present in the set, false otherwise.
     */
    public boolean insert(K key, V value);

    /**
     * Deletes the key-value pair from under the hash of {@code key}.
     * Returns true if the key was in the set, false otherwise
     */
    public boolean delete(K key);

    /**
     * Returns the value stored under {@code key}.
     * 
     * If {@code !has(key)}, this function must return {@code null}.
     */
    public V get(K key);

    /**
     * Checks if the {@code key} is in the hash map.
     * The function must be guaranteed to halt after one loop around the list.
     * 
     * Returns true if the it is, false otherwise.
     */
    public boolean has(K key);

    public int size();

    public boolean isEmpty();

    /**
     * Makes the set empty.
     * Returns true if the set was non-empty when the method-call was made, false otherwise.
     */
    public boolean clear();

    public Iterator<Entry<K, V>> items();

    public int hash(K key);
}
