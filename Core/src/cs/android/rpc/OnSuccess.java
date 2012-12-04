package cs.android.rpc;

import cs.android.IActivityWidget;
import cs.android.lang.ServerRequest;

public abstract class OnSuccess<RequestType extends ServerRequest> extends
		OnRequestEventBase<RequestType> {

	public OnSuccess(IActivityWidget parent, RequestType request) {
		super(parent, request);
		initiazlize(request.getOnSuccess().add(this));
	}

	public OnSuccess(RequestType request) {
		this(null, request);
	}

}