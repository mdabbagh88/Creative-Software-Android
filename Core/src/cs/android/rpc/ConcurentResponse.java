package cs.android.rpc;

import static cs.java.lang.Lang.list;
import cs.java.collections.List;

public class ConcurentResponse extends Response<Void> {

	private int _requestCount;
	private List<Response> _responses = list();

	public int requestCount() {
		return _requestCount;
	}

	public ConcurentResponse(Response<?>... responses) {
		addAll(responses);
	}

	public <T> Response<T> add(Response<T> response) {
		_responses.add(response);
		_requestCount++;
		new OnFailed<T>(response) {
			public void run() {
				failed(response());
				ConcurentResponse.this.cancel();
			}
		};
		new OnSuccess<T>(response) {
			public void run() {
				_requestCount--;
				if (_requestCount == 0) ConcurentResponse.this.success();
			}
		};
		return response;
	}

	public void cancel() {
		super.cancel();
		for (Response response : _responses)
			response.cancel();
	}

	public ConcurentResponse addAll(Response<?>... responses) {
		for (Response<?> response : responses)
			add(response);
		return this;
	}

	public void onAddDone() {
		if (_requestCount == 0) successLater();
	}

}
