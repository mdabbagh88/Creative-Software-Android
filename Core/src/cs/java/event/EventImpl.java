package cs.java.event;

import static cs.java.lang.Lang.exception;
import static cs.java.lang.Lang.list;
import cs.java.collections.List;
import cs.java.lang.Base;

public class EventImpl<T> extends Base implements Event<T> {

	class EventRegistrationImpl implements EventRegistration {
		Listener listener;

		public EventRegistrationImpl(Listener listener) {
			this.listener = listener;
		}

		@Override public void cancel() {
			int index = registrations.getIndex(this);
			if (index < 0) throw exception("Listener not found");
			if (running) toRemove.add(this);
			else registrations.remove(index);
		}

		@Override public Event<?> getEvent() {
			return EventImpl.this;
		}
	}
	private final List<EventRegistrationImpl> registrations = list();
	private final List<EventRegistrationImpl> toRemove = list();
	private final List<EventRegistrationImpl> toAdd = list();

	private boolean running;

	public EventImpl() {
	}

	@Override public EventRegistration add(final Listener listener) {
		EventRegistrationImpl registration = new EventRegistrationImpl(listener);
		if (running) toAdd.add(registration);
		else registrations.add(registration);
		return registration;
	}

	@Override public void run(T argument) {
		if (running) throw exception("Event runned while running");
		if (registrations.isEmpty()) return;

		running = true;
		for (EventRegistrationImpl registration : registrations)
			registration.listener.onEvent(this, registration, argument);
		for (EventRegistrationImpl registration : toRemove)
			registrations.delete(registration);
		toRemove.clear();
		for (EventRegistrationImpl registration : toAdd)
			registrations.add(registration);
		toAdd.clear();
		running = false;
	}

	public void clear() {
		registrations.clear();
	}

}