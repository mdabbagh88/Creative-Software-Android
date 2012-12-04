package cs.android.viewbase;

import cs.android.IActivityWidget;
import cs.java.event.Event;

public interface TabHostView extends IActivityWidget {

	AnimatorActivityViewer viewer();
	
	void displayView(int index);

	IActivityWidget getCurrentView();

	int getCurrentViewIndex();

	Event<Integer> getOnViewChange();

	int getStartViewIndex();

}
