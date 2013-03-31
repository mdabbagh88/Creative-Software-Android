package cs.android.rpc;

public class ConcurentResponse extends Response<Void> {

	private int _requestCount;

	public ConcurentResponse(Response<?>... responses) {
		addAll(responses);
	}

	public <T> Response<T> add(Response<T> response) {
		_requestCount++;
		new OnFailed<T>(response) {
			public void run() {
				failed(response().getFailedMessage());
				ConcurentResponse.this.cancel();
			}
		};
		new OnSuccess<T>(response) {
			public void run() {
				_requestCount--;
				if (_requestCount == 0) {
					ConcurentResponse.this.success();
					ConcurentResponse.this.cancel();
				}
			}
		};
		return response;
	}

	public ConcurentResponse addAll(Response<?>... responses) {
		for (Response<?> response : responses)
			add(response);
		return this;
	}
}
