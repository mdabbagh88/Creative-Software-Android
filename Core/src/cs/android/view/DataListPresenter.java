package cs.android.view;

import static cs.android.lang.AndroidLang.event;
import static cs.java.lang.Lang.fire;
import static cs.java.lang.Lang.info;
import static cs.java.lang.Lang.list;
import static cs.java.lang.Lang.no;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import cs.android.IActivityWidget;
import cs.android.viewbase.ActivityWidget;
import cs.java.collections.List;
import cs.java.event.Event;

public class DataListPresenter<RowType> extends ActivityWidget {
	private class ListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return getItemsCount();
		}

		@Override
		public Object getItem(int position) {
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			return listAdapterGetView(position, view);
		}
	}

	private final List<RowType> dataList = list();
	private final Event<List<RowType>> onLoad = event();
	private final ListAdapter listAdapter = new ListAdapter();
	private final RowFactory<RowType> viewFactory;
	private int savedSelectionIndex;

	public <Parent extends IActivityWidget & RowFactory<RowType>> DataListPresenter(Parent parent) {
		super(parent);
		this.viewFactory = parent;
	}

	public <Parent extends IActivityWidget & RowFactory<RowType>> DataListPresenter(Parent parent,
			int listViewId) {
		super(parent, listViewId);
		this.viewFactory = parent;
	}

	public void clear() {
		dataList.clear();
		listAdapter.notifyDataSetChanged();
	}

	public List<RowType> getData() {
		return dataList;
	}

	public Event<List<RowType>> getOnLoad() {
		return onLoad;
	}

	public void load(List<RowType> list) {
		dataList.addAll(list);
		listAdapter.notifyDataSetChanged();
		fire(onLoad, list);
	}

	public void prependData(RowType status) {
		dataList.add(0, status);
		listAdapter.notifyDataSetChanged();
	}

	public void refresh() {
		listAdapter.notifyDataSetChanged();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" }) public void reload(List<RowType> list) {
		saveSelection();
		dataList.clear();
		info(asAbsListView());
		((AdapterView) asAbsListView()).setAdapter(listAdapter);
		load(list);
		restoreSelection();
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

	public void setEmptyView(int layoutId) {
		asAbsListView().setEmptyView(inflateLayout(layoutId));
	}

	@Override protected void onPause() {
		super.onPause();
		saveSelection();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" }) @Override protected void onResume() {
		super.onResume();
		if (dataList.hasContents()) {
			((AdapterView) asAbsListView()).setAdapter(listAdapter);
			restoreSelection();
		}
	}

	private int getItemsCount() {
		return dataList.size();
	}

	private View listAdapterGetView(int position, View view) {
		RowView<RowType> rowView;
		if (no(view)) {
			rowView = viewFactory.create(position);
			view = rowView.asView();
			view.setTag(rowView);
		} else rowView = (RowView<RowType>) view.getTag();
		rowView.load(dataList.get(position));
		return view;
	}

}
