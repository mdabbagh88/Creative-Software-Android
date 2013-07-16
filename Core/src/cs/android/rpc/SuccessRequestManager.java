package cs.android.rpc;

import static cs.java.lang.Lang.SECOND;
import static cs.java.lang.Lang.no;
import cs.android.lang.DoLater;
import cs.android.view.NetworkReachability;
import cs.android.viewbase.ViewController;

public abstract class SuccessRequestManager<T> extends ViewController {

	private NetworkReachability reachability;

	public SuccessRequestManager(ViewController parent) {
		super(parent);
	}

	public void start() {
		if (isNetworkConnected()) process();
		else reachability = new NetworkReachability() {
			@Override protected void onNetworkConnected() {
				new DoLater(SuccessRequestManager.this, 3 * SECOND) {
					public void run() {
						process();
					}
				};
				stopReachability();
			}
		};
	}

	private void process() {
		new OnFailed<T>(createRequest()) {
			public void run() {
				new DoLater(SuccessRequestManager.this, 15 * SECOND) {
					public void run() {
						start();
					}
				};
			}
		};
	}

	private void stopReachability() {
		if (no(reachability)) return;
		reachability.stop();
		reachability = null;
	}

	protected abstract Response<T> createRequest();

	@Override protected void onPause() {
		super.onPause();
		stopReachability();
	}
}
