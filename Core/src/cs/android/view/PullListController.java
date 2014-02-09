package cs.android.view;

import android.os.Bundle;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cs.android.view.list.ListController;
import cs.android.viewbase.ViewController;

public class PullListController<RowType> extends ListController<RowType> {

	private PullToRefreshListView _refreshListView;
	private int _id;

	public <Parent extends ViewController & RowFactory<RowType>> PullListController(Parent parent,
			int id) {
		super(parent);
		_id = id;
	}

	protected void onCreate(Bundle state) {
		_refreshListView = (PullToRefreshListView) getView(_id);
		setView(_refreshListView.getRefreshableView());
		super.onCreate(state);
	}

	public PullToRefreshListView refreshListView() {
		return _refreshListView;
	}

	public void onRefreshComplete() {
		_refreshListView.onRefreshComplete();
	}

	public void setOnRefreshListener(OnRefreshListener<ListView> onRefreshListener) {
		_refreshListView.setOnRefreshListener(onRefreshListener);
	}

}
