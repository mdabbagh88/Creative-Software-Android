package cs.android.view;

import static cs.java.lang.Lang.info;
import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.no;
import android.view.ViewGroup;
import cs.android.R;
import cs.android.rpc.OnDone;
import cs.android.rpc.Response;
import cs.android.viewbase.ViewController;
import cs.java.lang.Call;
import cs.java.lang.Value;

public class ProgressController extends ViewController {

	int _inViewId;
	Response<?> _request;
	OnDone _onDone;

	public ProgressController(ViewController parent, int inViewId) {
		super(parent, layout(R.layout.load_indicator_dialog));
		_inViewId = inViewId;
	}

	void show() {
		info(isResumed());
		if (isResumed()) {
			fadeIn();
			((ViewGroup) _parent.asView()).addView(asView());
		}
	}

	public void hide() {
		fadeOut(asView(), new Call() {
			public void onCall(Object value) {
				((ViewGroup) _parent.asView()).removeView(asView());
			}
		});
	}

	private void onRequestDone() {
		_request = null;
		hide();
	}

	private void update() {
		if (no(_request)) hide();
		else if (_request.isDone()) onRequestDone();
		else {
			if (no(asView().getParent())) show();
			if (is(_onDone)) _onDone.cancel();
			_onDone = new OnDone(this, _request) {
				public void run() {
					onRequestDone();
				}
			};
		}
	}

	public void onBackPressed(Value<Boolean> goBack) {
		super.onBackPressed(goBack);
		if (is(_request)) {
			_request.cancel();
			hide();
		}
	}

	@Override protected void onPause() {
		super.onPause();
		hide();
	}

	@Override protected void onResume() {
		super.onResume();
		update();
	}

	public void setRequest(Response<?> request) {
		_request = request;
		update();
	}
}
