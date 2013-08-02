package cs.android.view;

import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.no;
import android.app.Dialog;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import cs.android.R;
import cs.android.rpc.OnDone;
import cs.android.rpc.Response;
import cs.android.viewbase.ViewController;

public class ProgressDialog extends ViewController {

	private Dialog progressDialog;
	private Response<?> request;
	private OnDone<?> onDone;

	public ProgressDialog(ViewController parent) {
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
		progressDialog = new Dialog(activity(), android.R.style.Theme_Light_NoTitleBar_Fullscreen);
		progressDialog.setCancelable(false);
		progressDialog.setContentView(R.layout.load_indicator_dialog);
		Window window = progressDialog.getWindow();
		window.setGravity(Gravity.BOTTOM);
		window.setLayout(LayoutParams.MATCH_PARENT, getDisplayHeight() - getStatusBarHeight());
		progressDialog.show();
	}

	private int getStatusBarHeight() {
		Rect rectgle = new Rect();
		Window window = activity().getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
		int statusBarHeight = rectgle.top;
		int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
		int titeBarHeight = contentViewTop - statusBarHeight;
		return titeBarHeight + statusBarHeight;
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
