package cs.android.rpc;

import cs.android.lang.ServerRequest;

public class ConcurentMultiRequest extends ServerRequestImpl {

	private int _requestCount;

	public ConcurentMultiRequest(ServerRequest... requests) {
		addAll(requests);
	}

	public ServerRequest addAll(ServerRequest... requests) {
		for (ServerRequest request : requests)
			add(request);
		return this;
	}

	private void add(ServerRequest request) {
		_requestCount++;
		new OnFailed<ServerRequest>(request) {
			public void run() {
				failed(request.getFailedMessage());
				ConcurentMultiRequest.this.cancel();
			}
		};
		new OnSuccess<ServerRequest>(request) {
			public void run() {
				_requestCount--;
				if (_requestCount == 0) {
					ConcurentMultiRequest.this.success();
					ConcurentMultiRequest.this.cancel();
				}
			}
		};
	}
}
