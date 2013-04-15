package cs.java.lang;

import java.util.Collection;
import java.util.Date;

import cs.android.lang.DoLaterProcess;
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
import cs.java.json.JSON;
import cs.java.json.JSONContainer;

public class Lang {

	public interface Work {
		void run();

		void start();

		void stop();
	}

	protected static LangCore impl;
	public static final boolean Yes = true;
	public static final boolean YES = true;
	public static final boolean No = false;
	public static final boolean NO = false;
	public static final int SECOND = 1000;
	public static final int HALFSECOND = 500;
	public static final int MINUTE = 60 * SECOND;

	public static final int HOUR = 60 * MINUTE;

	public static <T> void add(List<T> list, T... items) {
		for (T item : items)
			list.add(item);
	}

	public static void alert(String... messages) {
		impl.alert((Object[]) messages);
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

	public static String asString(Object value) {
		return String.valueOf(value);
	}

	public static String[] asStringArray(Object... dirs) {
		return string(",", dirs).split(",");
	}

	public static boolean between(int value, int from, int to) {
		return value >= from && value < to;
	}

	public static String createTraceString(Throwable ex) {
		return impl.createTraceString(ex);
	}

	public static long currentTime() {
		return new Date().getTime();
	}

	public static void debug(Object... values) {
		impl.debug(values);
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
		if (object == null) return Yes;
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
		return No;
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
		error(new Throwable(), values);
	}

	public static void error(Throwable e, Object... values) {
		impl.error(e, values);
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
		impl.info(values);
	}

	public static void initialize(LangCore impl) {
		Lang.impl = impl;
	}

	public static boolean is(boolean object) {
		return set(object);
	}

	public static boolean is(byte object) {
		return set(object);
	}

	public static boolean is(char object) {
		return set(object);
	}

	public static boolean is(double object) {
		return set(object);
	}

	public static boolean is(float object) {
		return set(object);
	}

	public static boolean is(int object) {
		return set(object);
	}

	public static boolean is(long object) {
		return set(object);
	}

	public static boolean is(Object item) {
		return item != null;
	}

	public static boolean is(Object... items) {
		for (Object item : items)
			if (!is(item)) return false;
		return true;
	}

	public static Iteration<Integer> iterate(int count) {
		return new GenericIterator<Integer>(count) {
			@Override protected Integer getValue() {
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

	public static JSON json() {
		return impl.json();
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

	public static boolean no(boolean object) {
		return empty(object);
	}

	public static boolean no(byte object) {
		return empty(object);
	}

	public static boolean no(char object) {
		return empty(object);
	}

	public static boolean no(double object) {
		return empty(object);
	}

	public static boolean no(float object) {
		return empty(object);
	}

	public static boolean no(int object) {
		return empty(object);
	}

	public static boolean no(long object) {
		return empty(object);
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

	public static Work schedule(final int delay_miliseconds, final Run runnable) {
		return impl.schedule(delay_miliseconds, runnable);
	}

	public static boolean set(CharSequence value) {
		return value == null ? false : value.length() > 0;
	}

	public static boolean set(Object... objects) {
		for (Object object : objects)
			if (empty(object)) return false;
		return true;
	}

	public static boolean set(Object value) {
		return !empty(value);
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

	public static void trace(Object... msg) {
		impl.trace(msg);
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
		return impl.urlEncode(argument);
	}

	public static void warn(Object... values) {
		impl.warn(values);
	}

}
