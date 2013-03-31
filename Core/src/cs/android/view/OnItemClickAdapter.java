package cs.android.view;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cs.android.viewbase.Widget;

public abstract class OnItemClickAdapter implements OnItemClickListener, OnClickListener {

	protected View _clickView;
	protected int _clickPosition;
	protected long _clickRowId;
	protected AdapterView<?> _clickAdapter;

	public OnItemClickAdapter(Widget<?> view, int listViewId) {
		view.getListView(listViewId).setOnItemClickListener(this);
	}

	public void onItemClick(AdapterView<?> adapter, View view, int position, long rowId) {
		_clickAdapter = adapter;
		_clickView = view;
		_clickPosition = position;
		_clickRowId = rowId;
		onClick(adapter);
	}

}
