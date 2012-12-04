package cs.java.json;

public interface JSONArray extends JSONContainer, Iterable<JSONType> {

	void add(boolean value);

	void add(double value);

	void add(JSONType value);

	void add(String value);

	JSONType get(int index);

	JSONArray getArray(int index);

	Boolean getBoolean(int index);

	Number getNumber(int index);

	JSONObject getObject(int index);

	String getString(int index);

	void set(int index, JSONType value);
}
