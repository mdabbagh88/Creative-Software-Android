package cs.java.json;

import cs.java.collections.Map;

public interface JSONObject extends JSONContainer, Iterable<String> {

	<T> Map<String, T> asMap(Class<T> valueType);

	boolean contains(String key);

	JSONType get(String name);

	JSONArray getArray(String key);

	Boolean getBoolean(String key);

	Boolean getBoolean(String key, Boolean defaultValue);

	Double getDouble(String key);

	Integer getInteger(String key);

	Integer getInteger(String key, Integer defaultValue);

	Long getLong(String key);

	Number getNumber(String key);

	JSONObject getObject(String key);

	String getString(String key);

	void put(String key, Boolean value);

	void put(String key, JSONType value);

	void put(String key, Number value);

	void put(String key, String value);

	void remove(String key);

}
