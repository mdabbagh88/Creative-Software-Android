package cs.java.collections;

public interface List<T> extends java.util.List<T> {
	List<T> add(T... items);

	T at(int index);

	boolean delete(T item);

	T first();

	int getIndex(T item);

	boolean hasSometing();

	@Override @Deprecated int indexOf(Object arg0);

	boolean isLast(T item);

	T last();

	int count();
	
	int lastIndex();

	List<T> range(int fromIndex);

	List<T> range(int fromIndex, int toIndex);

	@Override @Deprecated boolean remove(Object arg0);

	T removeLast();

	List<T> removeAll();
}
