package cs.android.rpc;

import static cs.java.lang.Lang.no;
import cs.android.viewbase.ViewController;
import cs.java.event.Event;
import cs.java.event.Event.EventRegistration;

public abstract class OnDone<Data> extends OnResponseBase<Data> {

	public OnDone(ViewController parent, Response<Data> request) {
		super(parent, request);
		if (no(request)) return;
		initiazlize(request.getOnDone().add(this));
	}

	public OnDone(Response<Data> request) {
		this(null, request);
	}

	@Override public <T> void onEvent(Event<T> event, EventRegistration registration, T argument) {
		super.onEvent(event, registration, argument);
	}

}