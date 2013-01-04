package cs.android.lang;

import static cs.java.lang.Lang.exception;
import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.string;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.util.Log;
import android.widget.Toast;
import cs.android.ApplicationContext;
import cs.android.json.JSONImpl;
import cs.java.json.JSON;
import cs.java.lang.Lang.Work;
import cs.java.lang.LangCore;
import cs.java.lang.LangCoreImplBase;
import cs.java.lang.Run;

public class AndroidLangCore extends LangCoreImplBase implements LangCore {

	private static final String LOG_TAG = "AndroidApplication";

	private JSON json;

	@Override public void alert(Object... messages) {
		Toast.makeText(ApplicationContext.getContext(), getAlertString(messages), Toast.LENGTH_LONG)
				.show();
	}

	@Override public void debug(Object... values) {
		Log.d(LOG_TAG, getDebugString(values));
	}

	@Override public void error(Throwable e, Object... values) {
		Log.e(LOG_TAG, ERROR_TITLE, e);
		Log.e(LOG_TAG, createLogMessage(ERROR_TITLE, "", values));
	}

	@Override public void info(Object... values) {
		StackTraceElement element = new Throwable().getStackTrace()[2];
		Log.i(LOG_TAG, createLogMessage(INFO_TITLE, createLogString(element), values));
	}

	@Override public void warn(Object... values) {
		StackTraceElement element = new Throwable().getStackTrace()[2];
		Log.w(LOG_TAG, createLogMessage(WARN_TITLE, createLogString(element), values));
	}

	@Override public JSON json() {
		if (is(json)) return json;
		return json = new JSONImpl();
	}

	@Override public Work schedule(final int delay_miliseconds, final Run runnable) {
		return new WorkImpl(delay_miliseconds, runnable);
	}

	@Override public void trace(Object... values) {
		Log.i(LOG_TAG, createLogMessage(INFO_TITLE, "", values), new Throwable());
	}

	@Override public String urlEncode(String argument) {
		try {
			return URLEncoder.encode(argument, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw exception(e);
		}
	}

	@Override protected String createLogString(StackTraceElement element) {
		return string(" ", element.getClassName(), element.getMethodName(), element.getFileName(),
				element.getLineNumber());
	}

}
