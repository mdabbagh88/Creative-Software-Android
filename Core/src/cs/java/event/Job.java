package cs.java.event;

import static cs.java.lang.Lang.list;
import cs.java.collections.List;
import cs.java.event.Event.EventRegistration;
import cs.java.lang.Run;

@SuppressWarnings("unchecked")
public abstract class Job<Argument> implements Run, Listener {

	protected Argument argument;
	protected EventRegistration registration;
	protected List<EventRegistration> registrations = list();
	protected Event<Argument> event;

	protected Job() {
	}

	public Job(Event<Argument>... events) {
		for (Event<Argument> event : events)
			registrations.add(event.add(this));
	}

	public Job(Event<Argument> event) {
		registrations.add(event.add(this));
	}

	public void cancel() {
		for (EventRegistration reg : registrations)
			reg.cancel();
		registrations.clear();
	}

	@Override
	public <T> void onEvent(Event<T> event, EventRegistration registration, T argument) {
		this.event = (Event<Argument>) event;
		this.registration = registration;
		this.argument = (Argument) argument;
		run();
	}

}