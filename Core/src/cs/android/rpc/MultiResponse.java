package cs.android.rpc;

import static cs.java.lang.Lang.is;

public class MultiResponse extends Response<Void> {

	private Response<?> _addedRequest;

	public <T> Response<T> add(final Response<T> response) {
		_addedRequest = response;
		new OnFailed<T>(response) {
			public void run() {
				failed(response.getFailedMessage());
				onDone();
			}
		};
		return response;
	}

	public <T> Response<T> addLast(final Response<T> response) {
		_addedRequest = response;
		new OnSuccess<T>(add(response)) {
			public void run() {
				success();
			}
		};
		return response;
	}

	@Override public void cancel() {
		super.cancel();
		if (is(_addedRequest)) _addedRequest.cancel();
	}

}
