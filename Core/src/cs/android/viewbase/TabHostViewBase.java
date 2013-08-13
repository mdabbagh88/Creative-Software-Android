package cs.android.viewbase;

import static cs.android.lang.AndroidLang.event;
import static cs.java.lang.Lang.empty;
import static cs.java.lang.Lang.fire;
import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.list;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import cs.android.view.adapter.OnClick;
import cs.java.collections.List;
import cs.java.event.Event;

public class TabHostViewBase extends ViewController {

	private class Tab {

		protected final TabViewFactory screen;
		protected final int tabViewId;

		public Tab(int tabViewId, TabViewFactory screen) {
			this.tabViewId = tabViewId;
			this.screen = screen;
		}

	}

	private final Event<Integer> onViewChange = event();
	private final AnimatorActivityViewer viewer;
	private final List<Tab> tabs = list();
	private static final String INDEX_STATE_KEY = "index_state_key";

	public AnimatorActivityViewer viewer() {
		return viewer;
	}

	private Integer currentIndex;

	public TabHostViewBase(ViewController parent, int layoutId, int animatorId) {
		super(parent, layout(layoutId));
		viewer = new AnimatorActivityViewer(this, animatorId);
	}

	public TabHostViewBase(LayoutId layoutId, int animatorId) {
		super(layoutId);
		viewer = new AnimatorActivityViewer(this, animatorId);
	}

	public void addTab(int tabViewId, TabViewFactory screen) {
		tabs.add(new Tab(tabViewId, screen));
	}

	public void displayView(int index) {
		CompoundButton currentTabView = getCompoundButton(tabs.get(currentIndex).tabViewId);
		currentTabView.setClickable(true);
		currentTabView.setChecked(false);

		viewer.displayView(createCurrentView(index), currentIndex < index);
		currentIndex = index;
		updateCurrentTab();
		onTabChange(index);
	}

	public ViewController getCurrentView() {
		return viewer.getCurrentView();
	}

	public int getCurrentViewIndex() {
		return currentIndex;
	}

	public Event<Integer> getOnViewChange() {
		return onViewChange;
	}

	public int getStartViewIndex() {
		return 0;
	}

	public void showNext() {
		if (currentIndex <= tabs.size()) displayView(currentIndex + 1);
	}

	public void showPrevious() {
		if (currentIndex > 0) displayView(currentIndex - 1);
	}

	protected void onCreate() {
		setTabClickListeners();
		super.onCreate();
	}

	protected void onCreate(Bundle state) {
		if (empty(currentIndex)) {
			if (is(state)) currentIndex = state.getInt(INDEX_STATE_KEY);
			else currentIndex = getStartViewIndex();
			viewer.setCurrentView(createCurrentView(currentIndex));
		} else viewer.setCurrentView(viewer.getCurrentView());
		updateCurrentTab();
		super.onCreate(state);
	}

	protected void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		state.putInt(INDEX_STATE_KEY, currentIndex);
	}

	protected void onTabChange(int index) {
		currentIndex = index;
		fire(onViewChange, index);
	}

	protected void onTabClick(final int index) {
		if (index == getCurrentViewIndex()) {
			getCompoundButton(tabs.get(index).tabViewId).setChecked(true);
			return;
		}
		displayView(index);
	}

	private ViewController createCurrentView(int index) {
		TabViewFactory tabView = tabs.get(index).screen;
		return tabView.createView(this, index);
	}

	private void setTabClickListeners() {
		for (int i = 0; i < tabs.size(); i++) {
			final int index = i;
			new OnClick(this, tabs.get(i).tabViewId) {
				public void onClick(View v) {
					onTabClick(index);
				}
			};
		}
	}

	private void updateCurrentTab() {
		CompoundButton nextTabView = getCompoundButton(tabs.get(currentIndex).tabViewId);
		nextTabView.setChecked(true);
	}

}
