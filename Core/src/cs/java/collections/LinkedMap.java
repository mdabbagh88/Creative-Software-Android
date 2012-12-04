package cs.java.collections;

public interface LinkedMap<K, V> extends Map<K, V> {

	V getValue(int index);

	int indexOf(K key);

}
