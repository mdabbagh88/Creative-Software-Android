package cs.android.rpc;

import cs.android.viewbase.ViewController;

public class OnResponse<Data> {

	private OnSuccess<Data> _success;

	public OnResponse(ViewController parent, Response response) {
		_success = new OnSuccess<Data>(parent, response) {
			public void run() {
				onSuccess(data());
				onSuccess();
			}
		};
		new OnFailed(parent, response) {
			public void run() {
				onFailed();
			}
		};
		new OnDone(parent, response) {
			public void run() {
				onDone();
			}
		};
	}

	public Data data() {
		return _success.data();
	}

	public OnResponse(Response response) {
		_success = new OnSuccess<Data>(response) {
			public void run() {
				onSuccess(data());
				onSuccess();
			}
		};
		new OnFailed(response) {
			public void run() {
				onFailed();
			}
		};
		new OnDone(response) {
			public void run() {
				onDone();
			}
		};
	}

	protected void onDone() {
	}

	protected void onFailed() {
	}

	protected void onSuccess(Data data) {
	}

	protected void onSuccess() {
	}

}
