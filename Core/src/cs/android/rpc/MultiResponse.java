package cs.android.rpc;

import static cs.java.lang.Lang.is;

public class MultiResponse<T> extends Response<T> {

	private Response<?> _addedRequest;

	public MultiResponse(final Response<?> response) {
		add(response);
	}

	public MultiResponse() {
	}

	public Response<?> add(final Response<?> response) {
		_addedRequest = response;
		new OnFailed(response) {
			public void run() {
				failed(response);
				onDone();
			}
		};
		return response;
	}

	public Response<T> addLast(final Response<T> response) {
		_addedRequest = response;
		new OnSuccess(add(response)) {
			public void run() {
				success((T) data());
			}
		};
		return response;
	}

	@Override public void cancel() {
		super.cancel();
		if (is(_addedRequest)) _addedRequest.cancel();
	}

}
