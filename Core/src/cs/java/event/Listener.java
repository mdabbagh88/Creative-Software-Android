package cs.java.event;

import cs.java.event.Event.EventRegistration;

public interface Listener {
	<T> void onEvent(Event<T> event, EventRegistration registration, T arg);
}
