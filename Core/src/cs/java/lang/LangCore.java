package cs.java.lang;

import cs.java.json.JSON;
import cs.java.lang.Lang.Work;

public interface LangCore {

	public static final String ERROR_TITLE = "Error";
	public static final String WARN_TITLE = "Warn";
	public static final String INFO_TITLE = "Info";

	void alert(Object... messages);

	String createTraceString(Throwable ex);

	void debug(Object... values);

	void doLater(int delay_miliseconds, Runnable runnable);

	void doLater(Runnable runnable);

	void error(Throwable e, Object... values);

	void warn(Object... values);

	void info(Object... values);

	JSON json();

	Work schedule(int delay_miliseconds, Run runnable);

	void trace(Object... values);

	String urlEncode(String argument);
}
