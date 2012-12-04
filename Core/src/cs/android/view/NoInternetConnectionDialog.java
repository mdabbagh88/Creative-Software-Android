package cs.android.view;

import static cs.java.lang.Lang.is;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import cs.android.viewbase.ActivityWidget;

public class NoInternetConnectionDialog extends ActivityWidget {

	private Dialog dialog;
	private final BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override public void onReceive(Context context, Intent intent) {
			onNetworkStateChange();
		}
	};

	public NoInternetConnectionDialog(ActivityWidget parent) {
		super(parent);
	}

	@Override protected void onPause() {
		super.onPause();
		context().unregisterReceiver(receiver);
		hideDialog();
	}

	@Override protected void onResume() {
		super.onResume();
		context().registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
		updateDialog();
	}

	private void hideDialog() {
		if (is(dialog)) {
			dialog.dismiss();
			dialog = null;
		}
	}

	private void onNetworkStateChange() {
		updateDialog();
	}

	private void showDialog() {
		if (is(dialog)) return;
		Builder builder = new Builder(context());
		builder.setMessage("No Internet Connection Aviable");
		builder.setCancelable(false);
		dialog = builder.create();
		dialog.show();
	}

	private void updateDialog() {
		if (isNetworkConnected())
			hideDialog();
		else showDialog();
	}

}
