package cs.android.viewbase;

import static cs.android.lang.AndroidLang.event;
import static cs.java.lang.Lang.fire;
import static cs.java.lang.Lang.list;
import static cs.java.lang.Lang.map;
import static cs.java.lang.Lang.no;

import java.util.ArrayList;

import android.os.Bundle;
import cs.java.collections.List;
import cs.java.collections.Map;
import cs.java.event.Event;
import cs.java.lang.Value;

public abstract class ScreenHostViewBase extends ViewController {

	private final Event<String> onViewChange = event();
	private final AnimatorActivityViewer viewer = new AnimatorActivityViewer(this, getAnimatorId());
	private final Map<String, ScreenFactory> screenFactories = map();
	private static final String HISTORY_STATE_KEY = "history_state_key";
	private List<String> viewHistory = list();
	private String currentScreenId;

	public ScreenHostViewBase(LayoutId layoutId) {
		super(layoutId);
	}

	public void addScreen(String id, ScreenFactory screen) {
		screenFactories.put(id, screen);
	}

	public void clearHistory() {
		String lastViewId = viewHistory.last();
		viewHistory.clear();
		viewHistory.add(getStartViewId(), lastViewId);
	}

	protected abstract String getStartViewId();

	public void displayNextView(String screenId) {
		if (viewHistory.contains(screenId)) {
			int viewIndex = viewHistory.getIndex(screenId);
			viewHistory = viewHistory.range(0, viewIndex + 1);
		} else viewHistory.add(screenId);
		viewer.displayView(createView(screenId), true);
		onScreenChange(screenId);
	}

	public ViewController getCurrentView() {
		return viewer.getCurrentView();
	}

	public String getCurrentViewId() {
		return currentScreenId;
	}

	public Event<String> getOnViewChange() {
		return onViewChange;
	}

	public void goBack() {
		viewHistory.removeLast();
		viewer.displayView(createView(viewHistory.last()), false);
		currentScreenId = viewHistory.last();
		onScreenChange(currentScreenId);
	}

	public boolean isStartViewVisible() {
		return getStartViewId() == getCurrentViewId();
	}

	public void onBackPressed(Value<Boolean> goBack) {
		super.onBackPressed(goBack);
		if (goBack.get()) if (viewHistory.size() > 1) {
			goBack();
			goBack.set(false);
		}
	}

	protected abstract int getAnimatorId();

	protected void onCreate(Bundle state) {
		if (viewHistory.isEmpty()) {
			if (no(state))
				viewHistory.add(getStartViewId());
			else viewHistory.addAll(state.getStringArrayList(HISTORY_STATE_KEY));
			viewer.setCurrentView(createView(viewHistory.last()));
		} else viewer.setCurrentView(viewer.getCurrentView());
		super.onCreate(state);
	}

	@SuppressWarnings("unchecked") protected void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		state.putStringArrayList(HISTORY_STATE_KEY, (ArrayList<String>) viewHistory);
	}

	protected void onScreenChange(String screenId) {
		fire(onViewChange, screenId);
	}

	private ViewController createView(String screenId) {
		currentScreenId = screenId;
		ScreenFactory screenView = screenFactories.value(screenId);
		return screenView.createView(this, screenId);
	}

}
