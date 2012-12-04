package cs.android.rpc;

import cs.android.IActivityWidget;
import cs.android.lang.ServerRequest;

public abstract class OnFailed<RequestType extends ServerRequest> extends
		OnRequestEventBase<RequestType> {

	public OnFailed(IActivityWidget parent, RequestType request) {
		super(parent, request);
		initiazlize(request.getOnFailed().add(this));
	}

	public OnFailed(RequestType request) {
		this(null, request);
	}

}