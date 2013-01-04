package cs.android.lang;

import android.os.Handler;

public class DoLaterProcess {
	private static final Handler handler = new Handler();
	private final Runnable _runnable;

	public DoLaterProcess(Runnable runnable, int delay_miliseconds) {
		_runnable = runnable;
		handler.postDelayed(_runnable, delay_miliseconds);
	}

	public DoLaterProcess(Runnable runnable) {
		_runnable = runnable;
		handler.post(_runnable);
	}

	public void stop() {
		handler.removeCallbacks(_runnable);
	}

}
