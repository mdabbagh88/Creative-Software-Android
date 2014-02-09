package cs.android.view.list;

import static cs.java.lang.Lang.YES;
import static cs.java.lang.Lang.event;
import static cs.java.lang.Lang.fire;
import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.list;
import static cs.java.lang.Lang.no;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import cs.android.view.RowView;
import cs.android.viewbase.ViewController;
import cs.android.viewbase.Widget;
import cs.java.collections.List;
import cs.java.event.Event;

public abstract class ListController<RowType> extends ViewController {
	private final List<RowType> _dataList = list();
	private final Event<List<RowType>> onLoad = event();
	private BaseAdapter _listAdapter = new ListAdapter(this);
	private int savedSelectionIndex;
	private Widget _emptyView;
	private boolean _firstLoad;

	public ListController(ViewController parent, int listViewId) {
		super(parent, listViewId);
	}

	public void clear() {
		_dataList.clear();
		reloadData();
	}

	public BaseAdapter adapter() {
		return _listAdapter;
	}
	
	protected abstract RowView<RowType> createView(int position, int viewType);

	public List<RowType> data() {
		return _dataList;
	}

	public Widget emptyView() {
		return _emptyView;
	}

	int getItemsCount() {
		return _dataList.size();
	}

	public int getItemTypesCount() {
		return 1;
	}

	protected int getItemViewType(int position) {
		return 0;
	}

	public Event<List<RowType>> getOnLoad() {
		return onLoad;
	}

	public View getRowView(int position, View view) {
		RowView<RowType> rowView;
		if (no(view)) {
			rowView = createView(position, getItemViewType(position));
			view = rowView.asView();
			view.setTag(rowView);
		} else rowView = (RowView<RowType>) view.getTag();
		rowView.load(_dataList.get(position));
		onRowViewLoaded(rowView);
		return view;
	}

	public ListController<RowType> loadAdd(List<RowType> list) {
		_firstLoad = YES;
		_dataList.addAll(list);
		// if (is(_emptyView)) asAbsListView().setEmptyView(_emptyView.asView());
		reloadData();
		fire(onLoad, list);
		return this;
	}

	@Override protected void onCreate() {
		super.onCreate();
		asAbsListView().setFastScrollEnabled(YES);
		asAbsListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListController.this.onItemClick(position, (RowView) view.getTag());
			}

		});
		asAbsListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				ListController.this.onItemLongClick(position, (RowView) view.getTag());
				return true;
			}
		});
	}

	protected void onItemClick(int position, RowView<RowType> view) {
	}

	protected void onItemLongClick(int position, RowView<RowType> view) {
	}

	@Override protected void onPause() {
		super.onPause();
		saveSelection();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" }) @Override protected void onResume() {
		super.onResume();
		if (_dataList.hasSometing()) {
			((AdapterView) asAbsListView()).setAdapter(_listAdapter);
			restoreSelection();
		}
	}

	protected void onRowViewLoaded(RowView<RowType> rowView) {
	}

	public void prependData(RowType status) {
		_dataList.add(0, status);
		reloadData();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" }) public void reload(List<RowType> list) {
		saveSelection();
		_dataList.clear();
		((AdapterView) asAbsListView()).setAdapter(_listAdapter);
		loadAdd(list);
		restoreSelection();
	}

	public void reloadData() {
		_listAdapter.notifyDataSetChanged();
		updateEmptyView();
	}

	public void restoreSelection() {
		asAbsListView().setSelection(savedSelectionIndex);
	}

	public void saveSelection() {
		savedSelectionIndex = asAbsListView().getSelectedItemPosition();
	}

	public void scrollToTop() {
		asAbsListView().setSelection(0);
		asAbsListView().dispatchTouchEvent(
				MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
						MotionEvent.ACTION_CANCEL, 0, 0, 0));
	}

	public Widget setEmptyView(int id) {
		return setEmptyView(new Widget(parent(), id));
	}

	public Widget setEmptyView(Widget view) {
		_emptyView = view;
		if (is(_emptyView)) _emptyView.hide();
		return _emptyView;
	}

	public ListController<RowType> setListAdapter(BaseAdapter listAdapter) {
		_listAdapter = listAdapter;
		return this;
	}

	private void updateEmptyView() {
		if (!_firstLoad) return;
		if (no(_emptyView)) return;
		if (_dataList.isEmpty()) _emptyView.show();
		else _emptyView.hide();
	}

}
