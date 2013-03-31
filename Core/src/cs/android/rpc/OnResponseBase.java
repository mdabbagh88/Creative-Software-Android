package cs.android.rpc;

import cs.android.IActivityWidget;
import cs.java.event.Event;
import cs.java.event.Event.EventRegistration;
import cs.java.event.Listener;
import cs.java.lang.Run;

public abstract class OnResponseBase<Data> implements Run, Listener {

	private final IActivityWidget onRequestParent;
	private Response<Data> response;
	private EventRegistration registration;

	public OnResponseBase(IActivityWidget parent, Response<Data> response) {
		onRequestParent = parent;
		this.response = response;
	}

	public void cancel() {
		registration.cancel();
	}

	public Data data() {
		return response.data();
	}

	@Override public <T> void onEvent(Event<T> event, EventRegistration registration, T argument) {
		if (onRequestParent == null || !onRequestParent.isPaused()) run();
	}

	public Response<Data> response() {
		return response;
	}

	protected void initiazlize(EventRegistration registration) {
		this.registration = registration;
	}
}