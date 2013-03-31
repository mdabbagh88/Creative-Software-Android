package cs.android.view;

import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.no;
import android.app.Dialog;
import cs.android.IActivityWidget;
import cs.android.R;
import cs.android.rpc.OnDone;
import cs.android.rpc.Response;
import cs.android.viewbase.ViewController;

public class ProgressDialog extends ViewController {

	private Dialog progressDialog;
	private Response<?> request;
	private OnDone<?> onDone;

	public ProgressDialog(IActivityWidget parent) {
		super(parent);
	}

	public void hideProgress() {
		if (is(progressDialog)) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	public void setRequest(Response<?> request) {
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
			onDone = new OnDone(this, request) {
				public void run() {
					onRequestDone();
				}
			};
		}
	}

	@Override protected void onPause() {
		super.onPause();
		hideProgress();
	}

	@Override protected void onResume() {
		super.onResume();
		update();
	}
}
