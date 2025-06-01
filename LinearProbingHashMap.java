import java.util.Iterator;

public class LinearProbingHashMap<K, V> extends OurAbstractHashMap<K, V> {
    public LinearProbingHashMap() {
        super();
    }

    public LinearProbingHashMap(int capacity, float loadFactor) {
        super(capacity, loadFactor);
    }

    @Override
    public boolean insert(K key, V value) {
        int index = hash(key);
        // or is it a collision (did something different hash to the same value)?
        boolean collision = !table[index].key.equals(key);

        if (!collision) {
            table[index].value = value;
        } else {
            while (table[index].state != EntryState.EMPTY || table[index].state != EntryState.TOMBSTONE) {
                index++;
                if (index > capacity) {
                    // TODO resize
                    throw new UnsupportedOperationException("Exceeded capacityi, resize not yet implemented");
                }
            }
        }

        table[index] = new Entry<K, V>(key, value);
        return true;
    }

    @Override
    public boolean delete(K key) {
        int index = hash(key);

        while (!key.equals(table[index].key)) {
            index++;
            if (index > capacity) return false;
        }

        table[index].key = null;
        table[index].key = null;
        table[index].state = EntryState.TOMBSTONE;
        return true;
    }

    @Override
    public V get(K key) {
        int index = hash(key);

        while (!key.equals(table[index].key)) {
            index++;
            if (index > capacity) return null;
        }

        return table[index].value;
    }

    @Override
    public boolean has(K key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'has'");
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'size'");
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isEmpty'");
    }

    @Override
    public boolean clear() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clear'");
    }

    @Override
    public Iterator<Entry<K, V>> items() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'items'");
    }

    @Override
    public int hash(K key) {
        int result = key.hashCode() % capacity;
        if (result < 0) {
            result += capacity;
        }

        return result;
    }
    
}
