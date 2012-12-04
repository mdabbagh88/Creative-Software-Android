package cs.java.lang;

import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.iterate;
import static cs.java.lang.Lang.string;
import static cs.java.lang.Lang.text;

public abstract class LangCoreImplBase implements LangCore {

	protected String createLogMessage(String title, String info, Object... values) {
		Text text = text(title).addSpace();
		for (Object object : values)
			text.add(String.valueOf(object)).addSpace();
		text.add(info);
		return text.toString();
	}

	protected String createLogString(StackTraceElement element) {
		return string("", element.getClassName(), ".", element.getMethodName(),
				"(" + element.getFileName(), ":", element.getLineNumber(), ")").toString();
	}

	@Override
	public String createTraceString(Throwable throwable) {
		Text text = text();
		if (is(throwable.getMessage()))
			text.add(throwable.getMessage()).addLine();
		else text.addLine();

		for (StackTraceElement element : iterate(throwable.getStackTrace()).skip(1))
			text.add(createLogString(element)).addLine();

		return text.toString();
	}

	protected String getAlertString(Object[] messages) {
		return string("", messages);
	}

	protected String getCallingStackInfo() {
		StackTraceElement element = new Throwable().getStackTrace()[4];
		return createLogString(element);
	}

	protected String getDebugString(Object... values) {
		return createLogMessage("Debug: ", getCallingStackInfo(), values);
	}

	protected String getErrorString(Throwable e, Object... values) {
		return createLogMessage(ERROR_TITLE, createTraceString(e), values);
	}

	protected String getInfoString(Object[] values) {
		return createLogMessage("Info: ", getCallingStackInfo(), values);
	}

	protected String getTraceString(Object... values) {
		return createLogMessage("Trace: ", createTraceString(new Throwable()), values);
	}

}