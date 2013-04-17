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
import cs.android.IActivityWidget;
import cs.java.event.Event;
import cs.java.event.EventImpl;
import cs.java.lang.Lang;

public class AndroidLang {
	private static Application aplication;

	public static void alert(int stringId) {
		Lang.alert(aplication.getString(stringId));
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

	public static Drawable getDrawable(IActivityWidget hasactivity, int drawable) {
		return hasactivity.activity().getResources().getDrawable(drawable);
	}

	public static Object invoke(Object object, String methodName) {
		try {
			return object.getClass().getMethod(methodName, (Class<?>[]) null).invoke(object);
		} catch (Exception e) {
			throw exception(e);
		}
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
