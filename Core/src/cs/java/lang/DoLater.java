package cs.java.lang;

import static cs.java.lang.Lang.doLater;

public abstract class DoLater implements Run {
	public DoLater() {
		doLater(this);
	}

	public DoLater(int miliseconds) {
		doLater(miliseconds, this);
	}

}
