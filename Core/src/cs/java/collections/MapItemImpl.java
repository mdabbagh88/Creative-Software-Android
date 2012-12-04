package cs.java.collections;

import java.util.Map;

public class MapItemImpl<K, V> implements MapItem<K, V> {
	private final Map<K, V> map;
	private final K key;

	public MapItemImpl(Map<K, V> map, K key) {
		this.map = map;
		this.key = key;
	}

	@Override
	public K key() {
		return key;
	}

	@Override
	public V value() {
		return map.get(key);
	}

}