package cs.android.view.adapter;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class OnScrollAdapter implements OnScrollListener {

	@Override public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
			int totalItemCount) {
		onScroll();
	}

	@Override public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	protected void onScroll() {
		// TODO Auto-generated method stub

	}

}
