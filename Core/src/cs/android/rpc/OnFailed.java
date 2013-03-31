package cs.android.rpc;

import static cs.java.lang.Lang.no;
import cs.android.IActivityWidget;

public abstract class OnFailed<Data> extends OnResponseBase<Data> {

	public OnFailed(IActivityWidget parent, Response<Data> request) {
		super(parent, request);
		if (no(request)) return;
		initiazlize(request.getOnFailed().add(this));
	}

	public OnFailed(Response<Data> request) {
		this(null, request);
	}

}