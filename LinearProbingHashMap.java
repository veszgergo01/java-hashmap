import java.util.Iterator;
import java.util.NoSuchElementException;

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
        size++;
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
        size--;
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
    
}
