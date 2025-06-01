public class Entry<K, V> {
    public K key;
    public V value;
    public EntryState state;

    /**
     * This constructor builds an Entry (key-value pair) naturally.
     */
    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
        state = EntryState.OCCUPIUED;
    }

    /**
     * This constructor can be used to create tombstones/empty entries.
     * 
     * @throws IllegalArgumentException if {@code entryState == EntryState.OCCUPIED}
     */
    public Entry(EntryState entryState) {
        if (entryState.equals(EntryState.OCCUPIUED)) {
            throw new IllegalArgumentException("Cannot create an falsely occupied entry.");
        }

        this.state = entryState;
    }
}