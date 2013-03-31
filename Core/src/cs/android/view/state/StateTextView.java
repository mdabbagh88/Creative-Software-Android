package cs.android.view.state;

import cs.android.IActivityWidget;
import cs.android.viewbase.ViewController;

public class StateTextView extends ViewController {

	private final int viewId;
	private String textValue = "";

	public StateTextView(IActivityWidget parent, int viewId) {
		super(parent);
		this.viewId = viewId;
	}

	@Override protected void onCreate() {
		super.onCreate();
		setTextValue(viewId, textValue);
	}

	@Override protected void onPause() {
		super.onPause();
		textValue = getTextValue(viewId);
	}

}
