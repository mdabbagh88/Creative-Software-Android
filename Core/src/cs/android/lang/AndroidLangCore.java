package cs.android.lang;

import static cs.java.lang.Lang.THOUSAND;
import static cs.java.lang.Lang.exception;
import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.string;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

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

	private JSON _json;
	private static StringBuilder _log = new StringBuilder();

	@Override public void alert(Object... messages) {
		Toast.makeText(ApplicationContext.getContext(), getAlertString(messages), Toast.LENGTH_LONG)
				.show();
	}

	@Override public void debug(Object... values) {
		String debugString = getDebugString(values);
		Log.d(AndroidLang.aplication().name(), debugString);
		addLogMessage(createLogMessage(DEBUG_TITLE, "", values));
	}

	public static StringBuilder log() {
		return _log;
	}

	private void addLogMessage(String debugString) {
		if (debugString.length() > 750) debugString = debugString.substring(0, 750);
		_log.append(new Date()).append(" ").append(debugString).append("\n\n");
		int trim = _log.length() - 200 * THOUSAND;
		if (trim > 0) _log.delete(0, trim);
	}

	@Override public void error(Throwable e, Object... values) {
		String message = createLogMessage(ERROR_TITLE, "", values);
		Log.e(AndroidLang.aplication().name(), ERROR_TITLE, e);
		Log.e(AndroidLang.aplication().name(), message);
		addLogMessage(createTraceString(e) + message);
	}

	@Override public void info(Object... values) {
		StackTraceElement element = new Throwable().getStackTrace()[2];
		String message = createLogMessage(INFO_TITLE, createLogString(element), values);
		Log.i(AndroidLang.aplication().name(), message);
		addLogMessage(message);
	}

	@Override public void warn(Object... values) {
		StackTraceElement element = new Throwable().getStackTrace()[2];
		String message = createLogMessage(WARN_TITLE, createLogString(element), values);
		Log.w(AndroidLang.aplication().name(), message);
		addLogMessage(message);
	}

	@Override public JSON json() {
		if (is(_json)) return _json;
		return _json = new JSONImpl();
	}

	@Override public Work schedule(final int delay_miliseconds, final Run runnable) {
		return new WorkImpl(delay_miliseconds, runnable);
	}

	@Override public void trace(Object... values) {
		String message = createLogMessage(INFO_TITLE, "", values);
		Throwable throwable = new Throwable();
		Log.i(AndroidLang.aplication().name(), message, throwable);
		addLogMessage(createTraceString(throwable) + message);
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

	public static void emptyLog() {
		_log.delete(0, _log.length());
	}

}
