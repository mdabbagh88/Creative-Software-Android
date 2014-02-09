package cs.android.view;

import static cs.java.lang.Lang.event;
import static cs.java.lang.Lang.fire;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import cs.android.viewbase.ContextController;
import cs.java.event.Event;

public class Reachability extends ContextController {

	private final BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override public void onReceive(Context context, Intent intent) {
			onNetworkStateChange();
		}
	};

	private final Event<Void> networkConnected = event();

	public Reachability() {
		start();
	}

	public void start() {
		context().registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
	}

	public void stop() {
		context().unregisterReceiver(receiver);
	}

	protected void onNetworkConnected() {
		fire(networkConnected);
	}

	private void onNetworkStateChange() {
		if (isNetworkConnected()) onNetworkConnected();
	}

}
