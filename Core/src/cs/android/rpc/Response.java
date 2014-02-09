package cs.android.rpc;

import static cs.java.lang.Lang.YES;
import static cs.java.lang.Lang.createTraceString;
import static cs.java.lang.Lang.debugAlert;
import static cs.java.lang.Lang.empty;
import static cs.java.lang.Lang.event;
import static cs.java.lang.Lang.exception;
import static cs.java.lang.Lang.fire;
import static cs.java.lang.Lang.is;
import android.app.Activity;
import cs.android.viewbase.ContextController;
import cs.java.event.Event;
import cs.java.event.Event.EventRegistration;
import cs.java.event.Listener;
import cs.java.lang.Run;
import cs.java.lang.RunWith;
import cs.java.net.Url;

public class Response<Data> extends ContextController {

	private final Event<Response<Data>> _onDone = event();

	private final Event<Response<?>> _onFailed = event();
	private final Event<Response<Data>> _onSuccess = event();
	private String _message;
	private String _stackTrace;
	private boolean _done;
	private boolean _success;
	private boolean _failed;
	protected Data _data;
	private boolean _canceled;
	private Url _url;
	private Activity _activity;
	private String _progressLabel;
	protected boolean _cacheable;
	protected boolean _reload;
	protected boolean _forceCache;

	public Response() {
	}

	public Response(Url url) {
		this._url = url;
	}

	protected Activity activity() {
		return _activity;
	}

	public void activity(Activity activity) {
		_activity = activity;
	}

	public Response<Data> onDone(final Run run) {
		getOnDone().add(new Listener() {
			public void onEvent(Event event, EventRegistration registration, Object arg) {
				run.run();
			}
		});
		return this;
	}
	
	public Response<Data> onSuccess(final Run run) {
		getOnSuccess().add(new Listener() {
			public void onEvent(Event event, EventRegistration registration, Object arg) {
				run.run();
			}
		});
		return this;
	}

	public Response<Data> addOnDone(final RunWith<Data> runWith) {
		getOnDone().add(new Listener() {
			public void onEvent(Event event, EventRegistration registration, Object arg) {
				runWith.run(_data);
			}
		});
		return this;
	}

	public Response<Data> addOnSuccess(final RunWith<Data> runWith) {
		getOnSuccess().add(new Listener() {
			public void onEvent(Event event, EventRegistration registration, Object arg) {
				runWith.run(_data);
			}
		});
		return this;
	}

	public Response<Data> cache(boolean forceCache) {
		_forceCache = forceCache;
		return this;
	}

	public void cancel() {
		if (_canceled || _done) return;
		_canceled = YES;
		onDoneImpl();
	}

	public String content() {
		return "";
	}

	public Data data() {
		return _data;
	}

	public void failed(final Exception e, String message) {
		if (_canceled) return;
		_message = message;
		if (is(e)) _stackTrace = createTraceString(e);
		onFailedImpl(this);
		onDoneImpl();
	}

	public void failed(Response response) {
		if (_canceled) return;
		onFailedImpl(response);
		onDoneImpl();
	}

	public void failed(final String message) {
		if (_canceled) return;
		_message = message;
		failed(this);
	}

	public <T> Response<T> failIfFail(final Response<T> response) {
		new OnFailed<T>(response) {
			public void run() {
				failed(response);
			}
		};
		return response;
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

	public String message() {
		return _message;
	}

	protected void onDone() {
	}

	protected final void onDoneImpl() {
		if (_done) throw exception();
		_done = YES;
		onDone();
		fire(_onDone, this);
	}

	protected void onFailed() {
	}

	protected final void onFailedImpl(Response<?> response) {
		debugAlert("Failed", url());
		if (_failed) throw exception("allready failed");
		_failed = YES;
		if (empty(_stackTrace)) _stackTrace = createTraceString(exception());
		_message = response.message();
		onFailed();
		fire(_onFailed, response);
	}

	protected void onSuccess() {
	}

	protected final void onSuccessImpl() {
		debugAlert("Success", url());
		if (_success) throw exception("allready success");
		_success = YES;
		onSuccess();
		fire(_onSuccess, this);
	}

	public String params() {
		return "";
	}

	public String progressLabel() {
		return _progressLabel;
	}

	public Response<Data> progressLabel(String progressLabel) {
		_progressLabel = progressLabel;
		return this;
	}

	public Response<Data> reload(boolean reload) {
		_reload = reload;
		return this;
	}

	public void reset() {
		_stackTrace = "";
		_message = "";
		_done = false;
		_success = false;
		_failed = false;
		_canceled = false;
	}

	public String stackTrace() {
		return _stackTrace;
	}

	public void success() {
		if (_canceled) return;
		onSuccessImpl();
		onDoneImpl();
	}

	public void success(Data data) {
		if (_canceled) return;
		_data = data;
		success();
	}

	public Url url() {
		return _url;
	}

}
