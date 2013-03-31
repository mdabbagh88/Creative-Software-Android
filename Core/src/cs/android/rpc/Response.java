package cs.android.rpc;

import static cs.android.lang.AndroidLang.event;
import static cs.java.lang.Lang.Yes;
import static cs.java.lang.Lang.createTraceString;
import static cs.java.lang.Lang.error;
import static cs.java.lang.Lang.fire;
import cs.android.viewbase.ContextPresenter;
import cs.java.event.Event;

public class Response<Data> extends ContextPresenter {

	private final Event<Response<Data>> _onDone = event();
	private final Event<Response<?>> _onFailed = event();
	private final Event<Response<Data>> _onSuccess = event();
	private String _exceptionMessage;
	private String _exceptionDetails;
	private boolean _done;
	private boolean _success;
	private boolean _failed;
	protected Data _data;
	private boolean _canceled;

	public Response() {
	}

	public void cancel() {
		_canceled = Yes;
		onDone();
	}

	public Data data() {
		return _data;
	}

	public void failed(final Exception e) {
		error(e);
		_exceptionDetails = createTraceString(e);
		failed(e.getMessage());
	}

	public void failed(final String message) {
		_exceptionMessage = message;
		onFailed(this);
		onDone();
	}

	public <T> Response<T> failIfFail(final Response<T> response) {
		new OnFailed<T>(response) {
			public void run() {
				failed(response.getFailedMessage());
			}
		};
		return response;
	}

	public String getExceptionDetails() {
		return _exceptionDetails;
	}

	public String getFailedMessage() {
		return _exceptionMessage;
	}

	public Event<Response<Data>> getOnDone() {
		return _onDone;
	}

	public Event<Response<?>> getOnFailed() {
		return _onFailed;
	}

	public Event<Response<Data>> getOnSuccess() {
		return _onSuccess;
	}

	public boolean isCanceled() {
		return _canceled;
	}

	public boolean isDone() {
		return _done;
	}

	public boolean isFailed() {
		return _failed;
	}

	public boolean isSuccess() {
		return _success;
	}

	public void onDone() {
		_done = true;
		fire(_onDone, Response.this);
	}

	public void onFailed(Response<?> request) {
		if (_canceled) return;
		_failed = true;
		fire(_onFailed, request);
	}

	public void onSuccess() {
		if (_canceled) return;
		_success = Yes;
		fire(_onSuccess, Response.this);
	}

	public void success(Data data) {
		_data = data;
		success();
	}

	protected void success() {
		onSuccess();
		onDone();
	}

}
