package cs.android.lang;

import static cs.java.lang.Lang.doLater;
import cs.android.IActivityWidget;
import cs.java.lang.Run;

public abstract class DoLater implements Run {

	public DoLater() {
		doLater(this);
	}

	public DoLater(final IActivityWidget parent, int miliseconds) {
		doLater(miliseconds, new Run() {
			@Override
			public void run() {
				if (parent.isResumed()) DoLater.this.run();
			}
		});
	}

	public DoLater(int miliseconds) {
		doLater(miliseconds, this);
	}

}
