package cs.android.lang;

import static cs.java.lang.Lang.MINUTE;
import static cs.java.lang.Lang.No;
import static cs.java.lang.Lang.Yes;
import static cs.java.lang.Lang.error;
import static cs.java.lang.Lang.exception;
import static cs.java.lang.Lang.info;
import static cs.java.lang.Lang.is;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.TimeZone;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import cs.android.BuildConfig;
import cs.android.viewbase.ViewController;
import cs.java.collections.GenericIterator;
import cs.java.collections.Iteration;
import cs.java.event.Event;
import cs.java.event.EventImpl;
import cs.java.lang.Lang;

public class AndroidLang {

	public static final Object INVOKE_FAILED = "invoke_failed";

	private static Application aplication;

	public static void alert(int stringId) {
		Lang.alert(aplication.getString(stringId));
	}

	public static boolean isDebug() {
		return BuildConfig.DEBUG;
	}

	public static boolean androidMinimum(int verCode) {
		if (android.os.Build.VERSION.RELEASE.startsWith("1.0"))
			return verCode == 1;
		else if (android.os.Build.VERSION.RELEASE.startsWith("1.1"))
			return verCode <= 2;
		else if (android.os.Build.VERSION.RELEASE.startsWith("1.5"))
			return verCode <= 3;
		else return android.os.Build.VERSION.SDK_INT >= verCode;
	}

	public static byte[] asArray(InputStream input) {
		final byte[] buffer = new byte[0x10000];
		int read;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Reader reader = null;
		try {
			while ((read = input.read(buffer, 0, buffer.length)) != -1)
				out.write(buffer, 0, read);
			out.flush();
			return out.toByteArray();
		} catch (Exception e) {
			throw exception(e);
		} finally {
			close(reader);
		}
	}

	public static String asString(InputStream input) {
		final char[] buffer = new char[0x10000];
		StringBuilder out = new StringBuilder();
		Reader reader = null;
		try {
			reader = new InputStreamReader(input, "UTF-8");
			int read;
			do {
				read = reader.read(buffer, 0, buffer.length);
				if (read > 0) out.append(buffer, 0, read);
			} while (read >= 0);
			return out.toString();
		} catch (Exception e) {
			error(e);
		} finally {
			close(reader);
		}
		return "";
	}

	public static void close(InputStream inputStream) {
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static <T> Event<T> event() {
		return new EventImpl<T>();
	}

	public static InputStream fileInput(String path) {
		FileInputStream inputStream = null;
		try {
			return new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (is(inputStream)) close(inputStream);
		}
		return null;
	}

	public static Application getAplication() {
		return aplication;
	}

	public static Drawable getDrawable(ViewController hasactivity, int drawable) {
		return hasactivity.activity().getResources().getDrawable(drawable);
	}

	public static Object invoke(Object object, String methodName) {
		try {
			return object.getClass().getMethod(methodName, (Class<?>[]) null).invoke(object);
		} catch (Exception e) {
			return INVOKE_FAILED;
		}
	}

	public static <T> Object invoke(Object object, String methodName, Class<?>[] types,
			Object[] argument) {
		try {
			return object.getClass().getMethod(methodName, types).invoke(object, argument);
		} catch (Exception e) {
			return INVOKE_FAILED;
		}
	}

	public static <T> Object invoke(Object object, String methodName, Class<T> type, T argument) {
		try {
			return object.getClass().getMethod(methodName, type).invoke(object, argument);
		} catch (Exception e) {
			return INVOKE_FAILED;
		}
	}

	public static Iteration<View> iterate(final ViewGroup layout) {
		return new GenericIterator<View>(layout.getChildCount()) {
			protected View getValue() {
				return layout.getChildAt(index());
			}
		};
	}

	public static boolean respondsTo(Object object, String methodName) {
		try {
			object.getClass().getMethod(methodName, (Class<?>[]) null);
			return Yes;
		} catch (NoSuchMethodException e) {
			return No;
		}
	}

	public static void setApplication(Application aplication) {
		AndroidLang.aplication = aplication;
	}

	public static void sleep(int delay) {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			error(e);
		}
	}

	public static long timeFrom(long time) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		info((calendar.getTimeInMillis() - time) / MINUTE);
		return System.currentTimeMillis() - time;
	}

	public static int to1E6(double value) {
		return (int) (value * 1E6);
	}

	public static String urlEncode(String argument) {
		try {
			return URLEncoder.encode(argument, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw exception(e);
		}
	}

	private static void close(Reader reader) {
		if (is(reader)) try {
			reader.close();
		} catch (IOException e) {
			error(e);
		}
	}
}
