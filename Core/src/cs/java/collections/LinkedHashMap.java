package cs.java.collections;

import static cs.java.lang.Lang.list;

public class LinkedHashMap<K, V> extends java.util.LinkedHashMap<K, V> implements LinkedMap<K, V> {
	@Override
	public V getValue(int index) {
		return list(values()).get(index);
	}

	@Override
	public boolean hasKey(K key) {
		return super.containsKey(key);
	};

	@Override
	public boolean hasValue(V value) {
		return super.containsValue(value);
	};

	@Override
	public int indexOf(K key) {
		return list(keySet()).getIndex(key);
	}

	public V removeAt(int i) {
		return remove(list(keySet()).get(i));
	}

	@Override
	public V value(K key) {
		return super.get(key);
	}

}
