package cs.android;

import static cs.java.lang.Lang.THOUSAND;
import static cs.java.lang.Lang.text;

import java.util.Date;

import android.util.Log;
import cs.android.lang.Application;
import cs.android.lang.CSLogger;
import cs.java.lang.Lang;
import cs.java.lang.Text;

public class CSMemoryLoggerImpl implements CSLogger {

	public static final String ERROR_TITLE = "Error";
	public static final String DEBUG_TITLE = "Debug";
	public static final String WARN_TITLE = "Warn";
	public static final String INFO_TITLE = "Info";

	protected static String createLogMessage(String title, String info, Object... values) {
		Text text = text(title).addSpace();
		for (Object object : values)
			text.add(String.valueOf(object)).addSpace();
		text.add(info);
		return text.toString();
	}

	protected static String getCallingStackInfo() {
		StackTraceElement element = new Throwable().getStackTrace()[5];
		return Lang.createLogString(element);
	}

	protected static String getDebugString(Object... values) {
		return createLogMessage(DEBUG_TITLE, getCallingStackInfo(), values);
	}

	protected static String getErrorString(Throwable e, Object... values) {
		return createLogMessage(ERROR_TITLE, Lang.createTraceString(e), values);
	}

	protected static String getInfoString(Object[] values) {
		return createLogMessage(INFO_TITLE, "", values);
	}

	private StringBuilder _log = new StringBuilder();

	private Application _application;

	public CSMemoryLoggerImpl(Application application) {
		_application = application;
	}

	public void addMemoryLogMessage(String debugString) {
		if (debugString.length() > THOUSAND) debugString = debugString.substring(0, THOUSAND);
		_log.append(new Date()).append(" ").append(debugString).append("\n\n");
		int trim = _log.length() - 9 * THOUSAND;
		if (trim > 0) _log.delete(trim, _log.length());
	}

	public void debug(Object... values) {
		String message = getDebugString(values);
		Log.d(_application.name(), message);
		addMemoryLogMessage(message);
	}

	public void emptyLog() {
		_log.delete(0, _log.length());
	}

	public void error(Object... values) {
		String message = createLogMessage(ERROR_TITLE, createLogString(), values);
		Log.e(_application.name(), message);
		addMemoryLogMessage(message);
	}

	public void error(Throwable e, Object... values) {
		String message = getErrorString(e, values);
		Log.e(_application.name(), message, e);
		addMemoryLogMessage(message);
	}

	public void info(Object... values) {
		String message = getInfoString(values);
		Log.i(_application.name(), message);
		addMemoryLogMessage(message);
	}

	public String log() {
		return _log.toString();
	}

	public void warn(Object... values) {
		String message = createLogMessage(WARN_TITLE, createLogString(), values);
		Log.w(_application.name(), message);
		addMemoryLogMessage(message);
	}

	private String createLogString() {
		return Lang.createLogString(new Throwable().getStackTrace()[2]);
	}
}