package cs.android.rpc;

import cs.android.IActivityWidget;
import cs.android.lang.ServerRequest;
import cs.java.event.Event;
import cs.java.event.Event.EventRegistration;
import cs.java.event.Listener;
import cs.java.lang.Run;

public abstract class OnRequestEventBase<RequestType extends ServerRequest> implements Run,
		Listener {

	protected final IActivityWidget onRequestParent;
	protected final RequestType request;
	private EventRegistration registration;

	public OnRequestEventBase(IActivityWidget parent, RequestType request) {
		onRequestParent = parent;
		this.request = request;
	}

	public void cancel() {
		registration.cancel();
	}

	 @Override
	public <T> void onEvent(Event<T> event, EventRegistration registration, T argument) {
		if (onRequestParent == null || !onRequestParent.isPaused()) run();
	}

	protected void initiazlize(EventRegistration registration) {
		this.registration = registration;
	}
}