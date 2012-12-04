package cs.android.viewbase;

import cs.android.IActivityWidget;
import cs.java.event.Event;

public interface ScreenHostView extends IActivityWidget {

	void clearHistory();

	void displayNextView(String id);

	IActivityWidget getCurrentView();

	String getCurrentViewId();

	Event<String> getOnViewChange();

	String getStartViewId();

}
