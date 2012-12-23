package cs.android.rpc;

import static cs.android.lang.AndroidLang.event;
import static cs.java.lang.Lang.Yes;
import static cs.java.lang.Lang.createTraceString;
import static cs.java.lang.Lang.error;
import static cs.java.lang.Lang.fire;
import cs.android.lang.ServerRequest;
import cs.android.viewbase.ContextPresenter;
import cs.java.event.Event;

public abstract class ServerRequestImpl extends ContextPresenter implements ServerRequest {

	private final Event<ServerRequest> _onDone = event();
	private final Event<ServerRequest> _onFailed = event();
	private final Event<ServerRequest> _onSuccess = event();
	private String _exceptionMessage;
	private String _exceptionDetails;
	private boolean _done;
	private boolean _success;
	private boolean _failed;
	private boolean _canceled;

	public boolean isCanceled() {
		return _canceled;
	}

	public ServerRequestImpl() {
	}

	@Override public String getExceptionDetails() {
		return _exceptionDetails;
	}

	@Override public String getFailedMessage() {
		return _exceptionMessage;
	}

	@Override public Event<ServerRequest> getOnDone() {
		return _onDone;
	}

	@Override public Event<ServerRequest> getOnFailed() {
		return _onFailed;
	}

	@Override public Event<ServerRequest> getOnSuccess() {
		return _onSuccess;
	}

	@Override public boolean isDone() {
		return _done;
	}

	public boolean isFailed() {
		return _failed;
	}

	@Override public boolean isSuccess() {
		return _success;
	}

	public void cancel() {
		_canceled = Yes;
		onDone();
	}

	public void onDone() {
		_done = true;
		fire(_onDone, ServerRequestImpl.this);
	}

	public void onFailed(ServerRequest request) {
		if (_canceled) return;
		_failed = true;
		fire(_onFailed, request);
	}

	public void onSuccess() {
		if (_canceled) return;
		_success = Yes;
		fire(_onSuccess, ServerRequestImpl.this);
	}

	protected void failed(final Exception e) {
		error(e);
		_exceptionDetails = createTraceString(e);
		failed(e.getMessage());
	}

	protected void failed(final String message) {
		_exceptionMessage = message;
		onFailed(this);
		onDone();
	}

	protected void success() {
		onSuccess();
		onDone();
	}

}
