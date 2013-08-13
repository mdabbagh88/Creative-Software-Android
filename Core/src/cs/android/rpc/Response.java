package cs.android.rpc;

import static cs.android.lang.AndroidLang.event;
import static cs.java.lang.Lang.Yes;
import static cs.java.lang.Lang.createTraceString;
import static cs.java.lang.Lang.doLater;
import static cs.java.lang.Lang.empty;
import static cs.java.lang.Lang.error;
import static cs.java.lang.Lang.exception;
import static cs.java.lang.Lang.fire;
import static cs.java.lang.Lang.is;
import android.app.Activity;
import cs.android.viewbase.ContextPresenter;
import cs.java.event.Event;
import cs.java.lang.Run;
import cs.java.net.Url;

public class Response<Data> extends ContextPresenter {

	private final Event<Response<Data>> _onDone = event();
	private final Event<Response<?>> _onFailed = event();
	private final Event<Response<Data>> _onSuccess = event();
	private String _exceptionMessage;
	private String _stackTrace;
	private boolean _done;
	private boolean _success;
	private boolean _failed;
	protected Data _data;
	private boolean _canceled;
	private Url url;
	private Activity _activity;

	public Response() {
	}

	protected Activity activity() {
		return _activity;
	}

	public void activity(Activity activity) {
		_activity = activity;
	}

	public Response(Url url) {
		this.url = url;
	}

	public void cancel() {
		if (_canceled) return;
		_canceled = Yes;
		onDone();
	}

	public String content() {
		return "";
	}

	public Data data() {
		return _data;
	}

	public void failed(final Exception e) {
		error(e);
		_stackTrace = createTraceString(e);
		failed(e.getMessage());
	}

	public void failed(final Exception e, String message) {
		error(e);
		_exceptionMessage = message;
		if (is(e)) _stackTrace = createTraceString(e);
		onFailed(this);
		onDone();
	}

	public void failed(final String message) {
		_exceptionMessage = message;
		failed(this);
	}

	public void failed(Response response) {
		if (_canceled) return;
		onFailed(response);
		onDone();
	}

	public <T> Response<T> failIfFail(final Response<T> response) {
		new OnFailed<T>(response) {
			public void run() {
				failed(response);
			}
		};
		return response;
	}

	public String getStackTrace() {
		return _stackTrace;
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
		if (_done) return;
		_done = true;
		fire(_onDone, this);
	}

	public void onFailed(Response<?> response) {
		if (_canceled) return;
		if (_failed) return;
		if (empty(_stackTrace)) _stackTrace = createTraceString(exception());
		_failed = true;
		fire(_onFailed, response);
	}

	public void onSuccess() {
		if (_canceled) return;
		if (_success) return;
		_success = Yes;
		fire(_onSuccess, this);
	}

	public String params() {
		return "";
	}

	public void reset() {
		_stackTrace = "";
		_exceptionMessage = "";
		_done = false;
		_success = false;
		_failed = false;
		_canceled = false;
	}

	public void success(Data data) {
		_data = data;
		success();
	}

	public Url url() {
		return url;
	}

	protected void success() {
		onSuccess();
		onDone();
	}

	public void successLater() {
		doLater(new Run() {
			public void run() {
				success();
			}
		});
	}

}
