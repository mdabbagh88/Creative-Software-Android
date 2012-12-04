package cs.android.view.state;

import cs.android.IActivityWidget;
import cs.android.viewbase.ActivityWidget;

public class StateCompoundButton extends ActivityWidget {

	private final int viewId;
	private boolean isChecked;

	public StateCompoundButton(IActivityWidget parent, int viewId) {
		super(parent);
		this.viewId = viewId;
	}

	@Override protected void onCreate() {
		super.onCreate();
		getCompoundButton(viewId).setChecked(isChecked);
	}

	@Override protected void onPause() {
		super.onPause();
		isChecked = getCompoundButton(viewId).isChecked();
	}

}
