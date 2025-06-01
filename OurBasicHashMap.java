import java.util.Iterator;

public class OurBasicHashMap<K, V> extends HashMapDefinition<K, V> {
    public OurBasicHashMap() {
        super();
    }

    public OurBasicHashMap(int capacity, float loadFactor) {
        super(capacity, loadFactor);
        //TODO Auto-generated constructor stub
    }

    @Override
    public boolean insert(K key, V value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public boolean delete(K key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public V get(K key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
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
    
}
