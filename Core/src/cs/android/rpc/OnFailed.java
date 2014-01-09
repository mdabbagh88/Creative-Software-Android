package cs.android.rpc;

import static cs.java.lang.Lang.no;
import cs.android.viewbase.ViewController;

public abstract class OnFailed<Data> extends OnResponseBase<Data> {

	public OnFailed(ViewController parent, Response<Data> response) {
		super(parent, response);
		if (no(response)) return;
		initiazlize(response.getOnFailed().add(this));
	}

	public OnFailed(Response<Data> response) {
		this(null, response);
	}

	protected Response failedReponse() {
		return _argument;
	}

}