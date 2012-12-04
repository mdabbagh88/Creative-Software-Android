package cs.java.event;

import static cs.java.lang.Lang.list;
import cs.java.collections.List;
import cs.java.event.Event.EventRegistration;
import cs.java.lang.Run;

public abstract class Task implements Run, Listener {

	protected Object argument;
	protected EventRegistration registration;
	protected List<EventRegistration> registrations = list();
	protected Event<?> event;

	public Task(Event<?>... events) {
		for (Event<?> event : events)
			registrations.add(event.add(this));
	}

	public void cancel() {
		for (EventRegistration reg : registrations)
			reg.cancel();
		registrations.clear();
	}

	@Override
	public <T> void onEvent(Event<T> event, EventRegistration registration, T argument) {
		this.event = event;
		this.registration = registration;
		this.argument = argument;
		run();
	}
}