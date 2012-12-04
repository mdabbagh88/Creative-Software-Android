package cs.java.event;

// public class EventWait extends Base implements Listener<Object> {
// private final List<Event<?>> waitingEvents = list();
//
// public Event<Void> eventFired() {
// return event("fired");
// }
//
// public List<Event<?>> getEvents() {
// return waitingEvents;
// }
//
// public void onEvent(Event<Object> event, Object arg) {
// waitingEvents.remove(event);
// if (empty(waitingEvents)) fire(eventFired());
// }
//
// public void waitFor(Event<?> event) {
// waitingEvents.add(event);
// listen(event);
// }
// }