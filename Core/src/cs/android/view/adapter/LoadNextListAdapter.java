package cs.android.view.adapter;

import static cs.java.lang.Lang.NO;
import static cs.java.lang.Lang.event;
import static cs.java.lang.Lang.fire;
import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.no;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import cs.android.view.list.ListController;
import cs.android.viewbase.ViewController;
import cs.android.viewbase.Widget;
import cs.java.collections.List;
import cs.java.event.Event;
import cs.java.event.Job;

public class LoadNextListAdapter extends ViewController {

	public class EndlessScrollListener implements OnScrollListener {
		private int visibleThreshold = 3;

		public EndlessScrollListener() {
		}

		public EndlessScrollListener(int visibleThreshold) {
			this.visibleThreshold = visibleThreshold;
		}

		@Override public void onScroll(AbsListView view, int first, int visible, int total) {
			if (totalItemCount() - visible <= 0 || loading) return;
			if (totalItemCount() - visible <= first + visibleThreshold) onLoadNext();
			LoadNextListAdapter.this.onScroll();
		}

		@Override public void onScrollStateChanged(AbsListView view, int scrollState) {

		}
	}

	private final Event<Void> onLoadNext = event();

	private final Widget<?> loadView;
	private EndlessScrollListener scrollListener;
	private boolean loading;
	private List<?> _data;

	public <T> LoadNextListAdapter(ListController<T> parent, int loadViewLayout) {
		super(parent);
		loadView = new Widget<View>(this, layout(loadViewLayout));
		new Job<List<T>>(parent.getOnLoad()) {
			@Override public void run() {
				onListLoad(argument);
			}
		};
	}

	public Event<?> getOnLoadNext() {
		return onLoadNext;
	}

	private void onListLoad(List<?> data) {
		_data = data;
		loadView.hide();
		if (no(scrollListener))
			scrollListener = new EndlessScrollListener();
		else {
			loading = false;
			if (data.isEmpty()) scrollListener = null;
		}
		updateScrollListener();
	}

	private void onLoadNext() {
		fire(onLoadNext);
		loading = true;
		loadView.show();
	}

	private int totalItemCount() {
		return is(_data) ? _data.size() : 0;
	}

	private void updateScrollListener() {
		asListView().setOnScrollListener(scrollListener);
	}

	@Override protected void onResume() {
		super.onResume();
		updateScrollListener();
		asListView().addFooterView(loadView.asView());
		asListView().setFooterDividersEnabled(NO);
	}

	protected void onScroll() {
	}
}
