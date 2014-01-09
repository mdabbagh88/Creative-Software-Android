package cs.android.view;

import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.no;
import android.view.ViewGroup;
import cs.android.rpc.OnDone;
import cs.android.rpc.Response;
import cs.android.viewbase.ViewController;
import cs.java.lang.Call;
import cs.java.lang.Value;

public class ProgressController extends ViewController {

	private int _topFrameId;
	private Response<?> _response;
	private OnDone _onDone;
	private int _labelId;

	public ProgressController(ViewController parent, int topFrameId, int layout, int label_id,
			int bar_id) {
		super(parent, layout(layout));
		_topFrameId = topFrameId;
		_labelId = label_id;
	}

	public void hideDialog() {
		fadeOut(asView(), new Call() {
			public void onCall(Object value) {
				((ViewGroup) findViewUp(_topFrameId)).removeView(asView());
			}
		});
	}

	public void onBackPressed(Value<Boolean> goBack) {
		super.onBackPressed(goBack);
		if (is(_response)) {
			_response.cancel();
			hideDialog();
		}
	}

	public void setResponse(Response<?> response) {
		if (is(_response)) return;
		_response = response;
		update();
	}

	public void showProgress() {
		if (isResumed()) {
			if (is(_response)) setText(_labelId, _response.progressLabel());
			fadeIn();
			((ViewGroup) findViewUp(_topFrameId)).addView(asView());
		}
	}

	private void onRequestDone() {
		_response = null;
		_onDone = null;
		hideDialog();
	}

	private void update() {
		if (no(_response)) hideDialog();
		else if (_response.isDone()) onRequestDone();
		else {
			if (no(asView().getParent())) showProgress();
			if (is(_onDone)) _onDone.cancel();
			_onDone = new OnDone(this, _response) {
				public void run() {
					onRequestDone();
				}
			};
		}
	}

	@Override protected void onPause() {
		super.onPause();
		hideDialog();
	}

	protected void onResume() {
		super.onResume();
		update();
	}
}
