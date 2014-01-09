package cs.android.lang;

public interface CSLogger {

	void debug(Object... values);

	void emptyLog();

	void error(Object... values);

	void error(Throwable e, Object... values);

	void info(Object... values);

	String log();

	void warn(Object... values);

}
