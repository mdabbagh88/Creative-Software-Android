package cs.android.rpc;

import cs.android.IActivityWidget;
import cs.android.lang.ServerRequest;

public abstract class OnDone<RequestType extends ServerRequest> extends
		OnRequestEventBase<RequestType> {

	public OnDone(IActivityWidget parent, RequestType request) {
		super(parent, request);
		initiazlize(request.getOnDone().add(this));
	}

	public OnDone(RequestType request) {
		this(null, request);
	}

}