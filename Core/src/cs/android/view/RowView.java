package cs.android.view;

import cs.android.viewbase.IsView;

public interface RowView<T> extends IsView {
	T data();

	void load(T row);
}
