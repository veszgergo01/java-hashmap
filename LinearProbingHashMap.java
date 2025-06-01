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
        final int initIndex = index;
        // is it a collision (did something different hash to the same value)?
        boolean collision = !key.equals(table[index].key);

        if (!collision) { // key was already in there
            table[index].value = value;
            return true;
        } else {
            while (EntryState.OCCUPIUED.equals(table[index].state)) {
                index = (index + 1) % capacity;
                // TODO infinite loop may occur
                if (index == initIndex) throw new UnsupportedOperationException("Fumbled the bag");
            }
        }

        table[index] = new Entry<K, V>(key, value);
        return false;
    }

    @Override
    public boolean delete(K key) {
        int index = hash(key);

        if (!has(key)) return false;

        /** Guaranteed to halt, due to contract of {@code this#has()} */
        while (!key.equals(table[index].key)) {
            index = (index + 1) % capacity;
        }

        table[index].key = null;
        table[index].key = null;
        table[index].state = EntryState.TOMBSTONE;
        return true;
    }

    @Override
    public V get(K key) {
        int index = hash(key);

        if (!has(key)) return null;

        /** Guaranteed to halt, due to contract of {@code this#has()} */
        while (!key.equals(table[index].key)) {
            index = (index + 1) % capacity;
        }

        return table[index].value;
    }

    @Override
    public boolean has(K key) {
        int index = hash(key);
        final int initIndex = index;

        while (!key.equals(table[index].key)) {
            index = (index + 1) % capacity;
            if (index == initIndex) return false;
        }

        return true;
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
