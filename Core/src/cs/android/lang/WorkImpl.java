package cs.android.lang;

import static cs.java.lang.Lang.doLater;
import static cs.java.lang.Lang.is;
import cs.java.lang.Lang.Work;
import cs.java.lang.Run;

public class WorkImpl implements Work {
	private boolean _stop = true;
	private final int _delay_miliseconds;
	private final Run _runnable;
	private DoLaterProcess _doLater;

	public WorkImpl(int delay_miliseconds, Run runnable) {
		_delay_miliseconds = delay_miliseconds;
		_runnable = runnable;
	}

	public void run() {
		_runnable.run();
	}

	public void start() {
		if (_stop) {
			_stop = false;
			process();
		}
	}

	public void stop() {
		_stop = true;
		if (is(_doLater)) _doLater.stop();
	}

	private void process() {
		_doLater = doLater(_delay_miliseconds, new Run() {
			public void run() {
				if (!_stop) {
					_runnable.run();
					process();
				}
			}
		});
	}
}
