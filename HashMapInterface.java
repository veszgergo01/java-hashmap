import java.util.Iterator;

/**
 * Method headers definition.
 */
public interface HashMapInterface<K, V> {
    public boolean insert(K key, V value);

    public boolean delete(K key);

    public V get(K key);

    public boolean has(K key);

    public int size();

    public boolean isEmpty();

    public boolean clear();

    public Iterator<Entry<K, V>> items();

    public int hash(K key);
}
