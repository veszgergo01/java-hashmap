import java.util.Iterator;

public interface OurMapInterface<K, V> {
    /**
     * Inserts {@code value} under the hash of {@code key}.
     * @return true if the key was already present in the set, false otherwise.
     */
    public boolean insert(K key, V value);

    /**
     * Deletes the key-value pair from under the hash of {@code key}.
     * @return true if the key was in the set, false otherwise
     */
    public boolean delete(K key);

    /**
     * @return the value stored under {@code key} or {@code null} if {@code !has(key)}.
     */
    public V get(K key);

    /**
     * Checks if the {@code key} is in the hash map.
     * The function must be guaranteed to halt after one loop around the list.
     * 
     * @return true if the it is, false otherwise.
     */
    public boolean has(K key);

    public int size();

    public boolean isEmpty();

    /**
     * Makes the set empty.
     * @return true if the set was non-empty when the method-call was made, false otherwise.
     */
    public boolean clear();

    public Iterator<Entry<K, V>> items();

    public int hash(K key);

    /**
     * Resizes the hash map.
     * Returns the capacity of the new hash table.
     */
    public int resize();
}
