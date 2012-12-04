package cs.java.collections;

public class HashMap<K, V> extends java.util.HashMap<K, V> implements Map<K, V> {

	@Override
	public boolean hasKey(K key) {
		return super.containsKey(key);
	}

	@Override
	public boolean hasValue(V value) {
		return super.containsValue(value);
	};

	@Override
	public V value(K key) {
		return super.get(key);
	};
}
