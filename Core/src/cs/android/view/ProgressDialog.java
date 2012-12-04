package cs.android.view;

import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.no;
import android.app.Dialog;
import cs.android.IActivityWidget;
import cs.android.R;
import cs.android.lang.ServerRequest;
import cs.android.rpc.OnDone;
import cs.android.viewbase.ActivityWidget;

public class ProgressDialog extends ActivityWidget {

	private Dialog progressDialog;
	private ServerRequest request;
	private OnDone<ServerRequest> onDone;

	public ProgressDialog(IActivityWidget parent) {
		super(parent);
	}

	public void hideProgress() {
		if (is(progressDialog)) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	public void setRequest(ServerRequest request) {
		this.request = request;
		update();
	}

	public void showProgress() {
		if (is(progressDialog)) return;
		progressDialog = new Dialog(activity(), R.style.LoadingDialogTheme);
		progressDialog.setCancelable(false);
		progressDialog.setContentView(R.layout.load_indicator_dialog);
		progressDialog.show();
	}

	@Override protected void onPause() {
		super.onPause();
		hideProgress();
	}

	@Override protected void onResume() {
		super.onResume();
		update();
	}

	private void onRequestDone() {
		request = null;
		hideProgress();
	}

	private void update() {
		if (no(request))
			hideProgress();
		else if (request.isDone())
			onRequestDone();
		else {
			if (no(progressDialog)) showProgress();
			if (is(onDone)) onDone.cancel();
			onDone = new OnDone<ServerRequest>(this, request) {
				@Override
				public void run() {
					onRequestDone();
				}
			};
		}
	}
}
