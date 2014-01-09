package cs.android.view.adapter;

import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import cs.android.viewbase.Widget;

public abstract class OnItemLongClickAdapter implements OnItemLongClickListener,
		OnLongClickListener {

	protected View _clickView;
	protected int _clickPosition;
	protected long _clickRowId;
	protected AdapterView<?> _clickAdapter;

	public OnItemLongClickAdapter(Widget<?> view, int listViewId) {
		view.getListView(listViewId).setOnItemLongClickListener(this);
	}

	public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long rowId) {
		_clickAdapter = adapter;
		_clickView = view;
		_clickPosition = position;
		_clickRowId = rowId;
		return onLongClick(view);
	}

}
