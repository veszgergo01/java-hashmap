public class QuadraticProbingHashMap<K, V> extends OurAbstractHashMap<K, V> {
    public QuadraticProbingHashMap() {
        super();
    }

    public QuadraticProbingHashMap(int capacity, float loadFactor) {
        super(capacity, loadFactor);
    }

    @Override
    public boolean insert(K key, V value) {
        int baseIndex = hash(key);
        int index;

        // Check if key already exists using quadratic probing
        for (int i = 0; i < capacity; i++) {
            index = (baseIndex + i * i) % capacity;

            // If the key already exists, update its value and return true
            if (table[index].state == EntryState.OCCUPIED && key.equals(table[index].key)) {
                table[index].value = value;
                return true; // updated value
            }
        }

        // If the key was not found, we use handleCollision to find the first available slot
        index = handleCollision(baseIndex);

        // Insert the new entry at the available slot
        table[index] = new Entry<>(key, value);
        size++;

        if ((float) size / capacity >= lambda) resize();
        return false;
    }


    @Override
    public boolean delete(K key) {
        if (!has(key)) return false;

        int baseIndex = hash(key);
        int index;

        for (int i = 0; i < capacity; i++) {
            index = (baseIndex + i * i) % capacity;

            if (key.equals(table[index].key)) {
                table[index].key = null;
                table[index].value = null;
                table[index].state = EntryState.TOMBSTONE;
                size--;
                return true;
            }
        }

        // Shouldn't reach this
        return false; 
    }

    @Override
    public V get(K key) {
        if (!has(key)) return null;

        int baseIndex = hash(key);
        int index;

        for (int i = 0; i < capacity; i++) {
            index = (baseIndex + i * i) % capacity;

            if (table[index].state == EntryState.OCCUPIED && key.equals(table[index].key)) {
                return table[index].value;
            }
        }
       
        // Shouldn't reach this
        return null; 
    }

    @Override
    public boolean has(K key) {
        int baseIndex = hash(key);
        int index;

        for (int i = 0; i < capacity; i++) {
            index = (baseIndex + i * i) % capacity;

            if (table[index].state == EntryState.EMPTY) {
                return false; // Key can't be in table beyond this
            }

            if (table[index].state == EntryState.OCCUPIED && key.equals(table[index].key)) {
                return true;
            }

        }

        return false;
    }

    @Override
    protected int handleCollision(int initialIndex) {
        int baseIndex = initialIndex;
        int index;

        for (int i = 0; i < capacity; i++) {
            index = (baseIndex + i * i) % capacity;

            if (table[index].state == EntryState.EMPTY || table[index].state == EntryState.TOMBSTONE) {
                return index;
            }
        }

        throw new RuntimeException("No available slot found during collision handling (table may be full)");
    }
}