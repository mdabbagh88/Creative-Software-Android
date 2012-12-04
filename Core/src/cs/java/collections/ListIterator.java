package cs.java.collections;

import java.util.List;

public class ListIterator<T> extends GenericIterator<T> {
	List<T> list;

	public ListIterator(List<T> list) {
		super(list.size());
		this.list = list;
	}

	@Override protected T getValue() {
		return list.get(index());
	}

	@Override protected void onRemove() {
		list.remove(index());
	}

}
