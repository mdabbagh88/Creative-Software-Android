package cs.android.view;

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

	private int _topFrameId;
	private Response<?> _request;
	private OnDone _onDone;

	public ProgressController(ViewController parent, int topFrameId) {
		super(parent, layout(R.layout.load_indicator_dialog));
		_topFrameId = topFrameId;
	}

	public void show() {
		if (isResumed()) {
			fadeIn();
			((ViewGroup) findViewUp(_topFrameId)).addView(asView());
		}
	}

	public void hide() {
		fadeOut(asView(), new Call() {
			public void onCall(Object value) {
				((ViewGroup) findViewUp(_topFrameId)).removeView(asView());
			}
		});
	}

	private void onRequestDone() {
		_request = null;
		_onDone = null;
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

	protected void onResume() {
		super.onResume();
		update();
	}

	public void setRequest(Response<?> request) {
		if (is(_request)) return;
		_request = request;
		update();
	}
}
