package cs.android.lang;

import cs.java.event.Event;

public interface ServerRequest {
	String getExceptionDetails();

	String getFailedMessage();

	Event<ServerRequest> getOnDone();

	Event<ServerRequest> getOnFailed();

	Event<ServerRequest> getOnSuccess();

	boolean isDone();

	boolean isSuccess();

}
