package cs.java.lang;

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
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import cs.android.BuildConfig;
import cs.android.CSApplication;
import cs.android.json.JSONImpl;
import cs.android.lang.Application;
import cs.android.lang.DoLaterProcess;
import cs.android.lang.WorkImpl;
import cs.android.model.Settings;
import cs.android.viewbase.ViewController;
import cs.java.collections.GenericIterator;
import cs.java.collections.HashMap;
import cs.java.collections.Iteration;
import cs.java.collections.LinkedHashMap;
import cs.java.collections.List;
import cs.java.collections.ListImpl;
import cs.java.collections.ListIterator;
import cs.java.collections.Map;
import cs.java.collections.MapIterator;
import cs.java.collections.Mapped;
import cs.java.event.Event;
import cs.java.event.EventImpl;
import cs.java.json.JSON;
import cs.java.json.JSONContainer;

public class Lang {

	public interface Work {
		void run();

		void start();

		void stop();
	}

	public static final boolean YES = true;

	public static final boolean NO = false;

	public static final int SECOND = 1000;
	public static final int HALFSECOND = 500;
	public static final int THOUSAND = 1000;
	public static final int MINUTE = 60 * SECOND;
	public static final int HOUR = 60 * MINUTE;
	public static final int DAY = 24 * HOUR;
	private static JSON _json;
	public static final Object INVOKE_FAILED = "invoke_failed";

	private static final String DEBUG_MODE = "DEBUG_MODE";
	private static Application _aplication;

	public static <T> void add(List<T> list, T... items) {
		for (T item : items)
			list.add(item);
	}

	public static void alert(int stringId) {
		Lang.alert(_aplication.getString(stringId));
	}

	public static void alert(Object... messages) {
		Toast.makeText(CSApplication.getContext(), getAlertString(messages), Toast.LENGTH_LONG).show();
		info(messages);
	}

	public static boolean androidMinimum(int verCode) {
		if (android.os.Build.VERSION.RELEASE.startsWith("1.0")) return verCode == 1;
		else if (android.os.Build.VERSION.RELEASE.startsWith("1.1")) return verCode <= 2;
		else if (android.os.Build.VERSION.RELEASE.startsWith("1.5")) return verCode <= 3;
		else return android.os.Build.VERSION.SDK_INT >= verCode;
	}

	public static Application aplication() {
		return _aplication;
	}

	public static Object[] array(Collection<?> list) {
		Object[] array = new Object[list.size()];
		int index = 0;
		for (Object object : list)
			array[index++] = object;
		return array;
	}

	public static <T> T[] array(T... items) {
		return items;
	}

	@SuppressWarnings("unchecked") public static <T> T as(Object object, Class<T> expectedClass) {
		try {
			return (T) object;
		} catch (Exception e) {
		}
		return null;
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

	public static double asDouble(Object value) {
		try {
			return Double.parseDouble(asString(value));
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public static int asInt(Object value) {
		try {
			return Integer.parseInt(asString(value));
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public static <T> void assertSet(T[] array) {
		if (empty(array)) throw exception("In Array should be something");
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

	public static String asString(Intent value) {
		String string = value.toString() + " Extras: ";
		for (String key : value.getExtras().keySet()) {
			Object info = value.getExtras().get(key);
			string += String.format("%s %s (%s)", key, info, is(info) ? info.getClass().getName() : "");
		}
		return string;
	}

	public static String asString(Object value) {
		return String.valueOf(value);
	}

	public static String[] asStringArray(Object... dirs) {
		return string(",", dirs).split(",");
	}

	public static boolean between(int value, int from, int to) {
		return value >= from && value < to;
	}

	public static void close(InputStream inputStream) {
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String createLogString(StackTraceElement element) {
		return string("", element.getClassName(), ".", element.getMethodName(),
				"(" + element.getFileName(), ":", element.getLineNumber(), ")").toString();
	}

	public static String createTraceString(Throwable throwable) {
		if (no(throwable)) return "";
		return Log.getStackTraceString(throwable);
	}

	public static long currentTime() {
		return new Date().getTime();
	}

	public static void debug(Object... values) {
		aplication().logger().debug(values);
	}

	public static void debugAlert(Object... messages) {
		if (isDebugMode())
			Toast.makeText(CSApplication.getContext(), getAlertString(messages), Toast.LENGTH_LONG)
					.show();
		info(messages);
	}

	public static DoLaterProcess doLater(int delay_miliseconds, final Runnable runnable) {
		return new DoLaterProcess(runnable, delay_miliseconds);
	}

	public static DoLaterProcess doLater(final Runnable runnable) {
		return new DoLaterProcess(runnable);
	}

	public static boolean empty(Object... objects) {
		for (Object object : objects)
			if (!empty(object)) return false;
		return true;
	}

	public static boolean empty(Object object) {
		if (object == null) return YES;
		if (object instanceof Number) return ((Number) object).floatValue() == 0;
		if (object instanceof Boolean) return !((Boolean) object).booleanValue();
		if (object instanceof CharSequence) return ((CharSequence) object).length() == 0;
		if (object instanceof Collection) return ((Collection<?>) object).size() == 0;
		if (object instanceof HasValue<?>) return empty(((HasValue<?>) object).get());
		if (object instanceof Map) return ((Map<?, ?>) object).size() == 0;
		if (object instanceof Object[]) return ((Object[]) object).length == 0;
		if (object instanceof int[]) return ((int[]) object).length == 0;
		if (object instanceof double[]) return ((double[]) object).length == 0;
		if (object instanceof long[]) return ((long[]) object).length == 0;
		if (object instanceof char[]) return ((char[]) object).length == 0;
		if (object instanceof float[]) return ((float[]) object).length == 0;
		if (object instanceof boolean[]) return ((boolean[]) object).length == 0;
		if (object instanceof byte[]) return ((byte[]) object).length == 0;
		return NO;
	}

	public static boolean equal(Object obj1, Object obj2) {
		if (obj1 == null) return obj2 == null;
		return obj1.equals(obj2);
	}

	public static boolean equalOne(Object object, Object... possibilities) {
		for (Object possibility : possibilities)
			if (equal(object, possibility)) return true;
		return false;
	}

	public static void error(Object... values) {
		aplication().logger().error(values);
	}

	public static void error(Throwable e, Object... values) {
		aplication().logger().error(e, values);
	}

	public static <T> Event<T> event() {
		return new EventImpl<T>();
	}

	public static RuntimeException exception(Exception ex) {
		return new RuntimeException(ex);
	}

	public static RuntimeException exception(Object... values) {
		return new RuntimeException(string("", values));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" }) public static <T> Field<T> field(String key) {
		return new FieldImpl(key);
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

	public static <T> void fire(Event<T> event, T argument) {
		event.run(argument);
	}

	public static void fire(Event<Void> eventVoid) {
		eventVoid.run(null);
	}

	public static void fire(Run eventVoid) {
		if (is(eventVoid)) eventVoid.run();
	}

	public static <T> T first(T[] items) {
		return list(items).first();
	}

	public static Drawable getDrawable(ViewController hasactivity, int drawable) {
		return hasactivity.activity().getResources().getDrawable(drawable);
	}

	public static Throwable getRoot(Throwable t) {
		Throwable result = t;
		while (result.getCause() != null)
			result = result.getCause();
		return result;
	}

	public static boolean has(List<?> list, Object... contents) {
		return has(list.toArray(), contents);
	}

	public static boolean has(Map<?, ?> map, Object... contents) {
		return has(array(map.values()), contents);
	}

	public static boolean has(Object[] array, Object... contents) {
		for (Object content : contents)
			if (!has(array, content)) return false;
		return true;
	}

	public static boolean has(String string, String... contents) {
		for (String content : contents)
			if (!string.contains(content)) return false;
		return true;
	}

	public static boolean hasOne(String string, String... contents) {
		for (String content : contents)
			if (string.contains(content)) return true;
		return false;
	}

	public static void info(Object... values) {
		aplication().logger().info(values);
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

	public static boolean is(Object... items) {
		if (items == null) return NO;
		for (Object item : items)
			if (!is(item)) return NO;
		return YES;
	}

	public static boolean isDebugBuild() {
		return BuildConfig.DEBUG;
	}

	public static boolean isDebugMode() {
		return Settings.get().loadBoolean(DEBUG_MODE);
	}

	public static Iteration<Integer> iterate(int count) {
		return new GenericIterator<Integer>(count) {
			protected Integer getValue() {
				return index();
			}
		};
	}

	public static int[] iterate(int[] integers) {
		if (no(integers)) return new int[0];
		return integers;
	}

	public static <T> Iteration<T> iterate(Iterable<T> items) {
		return new ListIterator<T>(list(items));
	}

	public static <K, V> Iteration<Mapped<K, V>> iterate(java.util.Map<K, V> map) {
		return new MapIterator<K, V>(map);
	}

	public static <T> Iteration<T> iterate(List<T> items) {
		return new ListIterator<T>(items);
	}

	public static <T> Iteration<T> iterate(T... items) {
		return iterate(list(items));
	}

	public static Iteration<View> iterate(final ViewGroup layout) {
		return new GenericIterator<View>(layout.getChildCount()) {
			protected View getValue() {
				return layout.getChildAt(index());
			}
		};
	}

	public static JSON json() {
		if (is(_json)) return _json;
		return _json = new JSONImpl();
	}

	public static JSONContainer json(String json_string) {
		return json().parse(json_string);
	}

	public static <T> T last(T[] items) {
		return list(items).last();
	}

	public static <K, V> LinkedHashMap<K, V> linkedmap() {
		return new LinkedHashMap<K, V>();
	}

	public static <T> List<T> list() {
		return new ListImpl<T>();
	}

	public static <T> List<T> list(Iterable<T> items) {
		List<T> list = list();
		if (is(items)) for (T item : items)
			list.add(item);
		return list;
	}

	public static <T> List<T> list(T... items) {
		List<T> list = list();
		add(list, items);
		return list;
	}

	public static <K, V> Map<K, V> map() {
		return new HashMap<K, V>();
	}

	public static Map<String, String> map(Object... values) {
		Map<String, String> map = map();
		for (int i = 0; i < values.length; i += 2)
			map.put(values[i] + "", values[i + 1] + "");
		return map;
	}

	public static boolean no(Object object) {
		return object == null;
	}

	public static boolean no(Object... objects) {
		if (objects == null) return true;
		for (Object object : objects)
			if (is(object)) return false;
		return true;
	}

	public static boolean respondsTo(Object object, String methodName) {
		try {
			object.getClass().getMethod(methodName, (Class<?>[]) null);
			return YES;
		} catch (NoSuchMethodException e) {
			return NO;
		}
	}

	public static Work schedule(final int delay_miliseconds, final Run runnable) {
		return new WorkImpl(delay_miliseconds, runnable);
	}

	public static boolean set(CharSequence value) {
		return value == null ? false : value.length() > 0;
	}

	public static boolean set(Object... objects) {
		if (objects == null) return NO;
		for (Object object : objects)
			if (empty(object)) return false;
		return true;
	}

	public static boolean set(Object value) {
		return !empty(value);
	}

	public static void setApplication(Application aplication) {
		_aplication = aplication;
	}

	public static void setDebug(boolean value) {
		Settings.get().save(DEBUG_MODE, value);
	}

	public static void sleep(int delay) {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			error(e);
		}
	}

	public static String string(String separator, Object... values) {
		Text text = text();
		for (Object object : values)
			if (set(object)) text.add(String.valueOf(object)).add(separator);
		if (!text.isEmpty()) text.cutEnd(separator.length());
		return text.toString();
	}

	public static String string(String separator, String... values) {
		Text text = text();
		for (String string : values)
			if (set(string)) text.add(string).add(separator);
		if (!text.isEmpty()) text.cutEnd(separator.length());
		return text.toString();
	}

	public static Text text(String... values) {
		return new TextImpl(values);
	}

	public static long timeFrom(long time) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		info((calendar.getTimeInMillis() - time) / MINUTE);
		return System.currentTimeMillis() - time;
	}

	public static int to1E6(double value) {
		return (int) (value * 1E6);
	}

	public static boolean unequal(Object obj1, Object obj2) {
		return !equal(obj1, obj2);
	}

	public static RuntimeException unexpected(Object... msg) {
		return new RuntimeException(text("Unexpected").add(msg).toString());
	}

	public static RuntimeException unimplemented(Object... msg) {
		return new RuntimeException(text("Unimplemented").add(msg).toString());
	}

	public static RuntimeException unsuported(Object... msg) {
		return new RuntimeException(text("Unsupported").add(msg).toString());
	}

	public static String urlEncode(String argument) {
		try {
			return URLEncoder.encode(argument, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw exception(e);
		}
	}

	public static void warn(Object... values) {
		aplication().logger().warn(values);
	}

	private static void close(Reader reader) {
		if (is(reader)) try {
			reader.close();
		} catch (IOException e) {
			error(e);
		}
	}

	private static boolean is(Object item) {
		return item != null;
	}

	protected static String getAlertString(Object[] messages) {
		return string(" ", messages);
	}

}
