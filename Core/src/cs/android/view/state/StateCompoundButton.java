package cs.android.view.state;

import cs.android.viewbase.ViewController;

public class StateCompoundButton extends ViewController {

	private final int viewId;
	private boolean isChecked;

	public StateCompoundButton(ViewController parent, int viewId) {
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
