package cs.android.rpc;

import static cs.android.lang.AndroidLang.event;
import static cs.java.lang.Lang.Yes;
import static cs.java.lang.Lang.createTraceString;
import static cs.java.lang.Lang.error;
import static cs.java.lang.Lang.fire;
import cs.android.lang.ServerRequest;
import cs.android.viewbase.ContextPresenter;
import cs.java.event.Event;

public abstract class ServerRequestBase extends ContextPresenter implements ServerRequest {

	protected final Event<ServerRequest> onDone = event();
	protected final Event<ServerRequest> onFailed = event();
	protected final Event<ServerRequest> onSuccess = event();
	protected String exceptionMessage;
	protected String exceptionDetails;
	protected boolean isDone;
	protected boolean isSuccess;
	protected boolean isFailed;

	public ServerRequestBase() {
	}

	@Override
	public String getExceptionDetails() {
		return exceptionDetails;
	}

	@Override
	public String getFailedMessage() {
		return exceptionMessage;
	}

	@Override
	public Event<ServerRequest> getOnDone() {
		return onDone;
	}

	@Override
	public Event<ServerRequest> getOnFailed() {
		return onFailed;
	}

	@Override
	public Event<ServerRequest> getOnSuccess() {
		return onSuccess;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	public boolean isFailed() {
		return isFailed;
	}

	@Override
	public boolean isSuccess() {
		return isSuccess;
	}

	public void onDone() {
		isDone = true;
		fire(onDone, ServerRequestBase.this);
	}

	public void onFailed(ServerRequest request) {
		isFailed = true;
		fire(onFailed, request);
	}

	public void onSuccess() {
		isSuccess = Yes;
		fire(onSuccess, ServerRequestBase.this);
	}

	protected void failed(final Exception e) {
		error(e);
		exceptionDetails = createTraceString(e);
		failed(e.getMessage());
	}

	protected void failed(final String message) {
		exceptionMessage = message;
		onFailed(this);
		onDone();
	}

	protected void success() {
		onSuccess();
		onDone();
	}

}
