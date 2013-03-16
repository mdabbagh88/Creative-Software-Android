package cs.android.rpc;

import static cs.java.lang.Lang.doLater;
import static cs.java.lang.Lang.is;
import cs.android.lang.ServerRequest;
import cs.java.lang.Run;

public class MultiRequest extends ServerRequestImpl {

	private ServerRequest _addedRequest;

	public ServerRequest add(ServerRequest request) {
		_addedRequest = request;
		new OnFailed<ServerRequest>(request) {
			public void run() {
				failed(request.getFailedMessage());
				onDone();
			}
		};
		return request;
	}

	@Override public void cancel() {
		super.cancel();
		if (is(_addedRequest)) _addedRequest.cancel();
	}
	
	@Override public void failed(String message) {
		// TODO Auto-generated method stub
		super.failed(message);
	}

	public ServerRequest addLast(ServerRequest request) {
		_addedRequest = request;
		new OnSuccess<ServerRequest>(add(request)) {
			public void run() {
				success();
			}
		};
		return request;
	}

	public void finish() {
		doLater(new Run() {
			public void run() {
				success();
			}
		});
	}

}
