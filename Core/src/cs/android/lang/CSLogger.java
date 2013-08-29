package cs.android.lang;

public interface CSLogger {

	void emptyLog();

	String log();

	void debug(Object... values);

	void error(Object... values);

	void error(Throwable e, Object... values);

	void info(Object... values);

	void trace(Object... values);

	void warn(Object... values);

}
