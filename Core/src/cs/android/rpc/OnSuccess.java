package cs.android.rpc;

import static cs.java.lang.Lang.no;
import cs.android.IActivityWidget;

public abstract class OnSuccess<Data> extends OnResponseBase<Data> {

	public OnSuccess(IActivityWidget parent, Response<Data> request) {
		super(parent, request);
		if (no(request)) return;
		initiazlize(request.getOnSuccess().add(this));
	}

	public OnSuccess(Response<Data> request) {
		this(null, request);
	}

}