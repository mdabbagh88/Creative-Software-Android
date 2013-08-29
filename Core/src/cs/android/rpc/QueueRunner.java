package cs.android.rpc;

import static cs.java.lang.Lang.sleep;
import static cs.java.lang.Lang.iterate;
import static cs.java.lang.Lang.list;

import java.util.concurrent.atomic.AtomicBoolean;

import android.os.Handler;
import android.os.Looper;
import cs.java.collections.List;
import cs.java.lang.Run;

public class QueueRunner {

	protected Handler threadHandler;
	private final AtomicBoolean isHandlerReady = new AtomicBoolean(false);
	private final List<Run> waitingRequests = list();

	public QueueRunner() {
		new Thread() {
			@Override public void run() {
				startThreadHandler();
			};
		}.start();
	}

	public void postAtFrontOfQueue(final Runnable request) {
		threadHandler.postAtFrontOfQueue(request);
	}

	public void send(final Run request) {
		if (isHandlerReady.get())
			threadHandler.post(request);
		else waitingRequests.add(request);
	}

	private void startThreadHandler() {
		Looper.prepare();
		threadHandler = new Handler();
		isHandlerReady.set(true);
		sleep(50);
		for (final Run request : iterate(waitingRequests).reverse())
			threadHandler.postAtFrontOfQueue(request);
		Looper.loop();
	}
}
