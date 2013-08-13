package cs.android.lang;

import static cs.java.lang.Lang.exception;
import android.os.Handler;

public class DoLaterProcess {
	private static final Handler handler = new Handler();
	private final Runnable _runnable;

	public DoLaterProcess(Runnable runnable, int delay_miliseconds) {
		_runnable = runnable;
		if (!handler.postDelayed(_runnable, delay_miliseconds)) throw exception();
	}

	public DoLaterProcess(Runnable runnable) {
		_runnable = runnable;
		if (!handler.post(_runnable)) throw exception();
	}

	public void stop() {
		handler.removeCallbacks(_runnable);
	}

}
