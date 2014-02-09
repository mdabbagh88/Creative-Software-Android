package cs.java.event;

public interface Event<T> {

	public interface EventRegistration {
		void cancel();

		Event<?> getEvent();
	}

	EventRegistration add(Listener listener);

	void run(T arg);

	void clear();
}