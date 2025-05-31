import java.util.Iterator;

public interface HashMapDefinition<K, V> {
    public boolean insert(K key, V value);

    public boolean delete(K key);

    public V get(K key);

    public boolean has(K key);

    public int size();

    public boolean isEmpty();

    public boolean clear();

    public Iterator<Entry<K, V>> items();
}
