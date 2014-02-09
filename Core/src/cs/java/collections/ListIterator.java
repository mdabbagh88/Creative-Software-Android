package cs.java.collections;

import java.util.List;

public class ListIterator<T> extends GenericIterator<T> {
	private List<T> _list;

	public ListIterator(List<T> list) {
		super(list.size());
		this._list = list;
	}

	@Override protected T getValue() {
		return _list.get(index());
	}

	@Override protected void onRemove() {
		_list.remove(index());
	}

}
