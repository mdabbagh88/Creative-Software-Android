package cs.android.rpc;

import static cs.java.lang.Lang.no;
import cs.android.IActivityWidget;

public abstract class OnDone<Data> extends OnResponseBase<Data> {

	public OnDone(IActivityWidget parent, Response<Data> request) {
		super(parent, request);
		if (no(request)) return;
		initiazlize(request.getOnDone().add(this));
	}

	public OnDone(Response<Data> request) {
		this(null, request);
	}

}