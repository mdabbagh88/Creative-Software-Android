package cs.java.collections;

import static cs.java.lang.Lang.list;

import java.util.List;
import java.util.Map;

public class MapIterator<K, V> extends GenericIterator<Mapped<K, V>> implements Mapped<K, V> {

	private final List<K> keys;
	private int last_index = -1;
	private final Map<K, V> map;

	public MapIterator(Map<K, V> map) {
		super(map.size());
		this.map = map;
		this.keys = list(map.keySet());
	}

	@Override
	public MapItem<K, V> get() {
		if (last_index != index()) last_index = index();
		return this;
	}

	protected MapItem<K, V> getItem(int index) {
		return new MapItemImpl<K, V>(map, keys.get(index));
	}

	@Override
	public MapItem<K, V> getNext() {
		return getItem(index() + 1);
	}

	@Override
	public MapItem<K, V> getPrevious() {
		return getItem(index() - 1);
	}

	@Override protected Mapped<K, V> getValue() {
		return this;
	}

	@Override
	public K key() {
		return keys.get(index());
	}

	@Override public void onRemove() {
		removeItem(index());
		last_index = -1;
	}

	protected void removeItem(int index) {
		map.remove(keys.get(index));
		keys.remove(index);
	}

	@Override
	public V value() {
		return map.get(key());
	}
}
