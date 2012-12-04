package cs.java.event;

public abstract class TaskWhen<Argument> implements Runnable, Listener {
	private final Argument argument;

	public TaskWhen(Argument argument) {
		this.argument = argument;
	}

	public Argument getArgument() {
		return this.argument;
	}

	public void onEvent(Event<Argument> src, Argument argument) {
		if (argument.equals(argument)) run();
	}

}