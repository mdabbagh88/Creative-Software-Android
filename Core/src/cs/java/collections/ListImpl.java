package cs.java.collections;

import java.util.ArrayList;

public class ListImpl<T> extends ArrayList<T> implements List<T> {

	public ListImpl() {
		super();
	}

	public ListImpl(java.util.List<T> list) {
		super(list);
	}

	@Override public List<T> add(T... items) {
		for (T item : items)
			add(item);
		return this;
	}

	@Override public T at(int index) {
		if (index < size() && index >= 0) return get(index);
		return null;
	}

	@Override public boolean delete(T item) {
		return remove(item);
	}

	@Override public T first() {
		return at(0);
	}

	@Override public int getIndex(T item) {
		return indexOf(item);
	}

	@Override public boolean hasContents() {
		return size() > 0;
	}

	public boolean isLast(T item) {
		return last() == item;
	}

	@Override public T last() {
		return at(lastIndex());
	}

	@Override public int lastIndex() {
		return size() - 1;
	}

	@Override public List<T> range(int fromIndex) {
		return range(fromIndex, size());
	}

	@Override public List<T> range(int fromIndex, int toIndex) {
		return new ListImpl<T>(subList(fromIndex, toIndex));
	}

	public T removeLast() {
		return remove(size() - 1);
	}
}
