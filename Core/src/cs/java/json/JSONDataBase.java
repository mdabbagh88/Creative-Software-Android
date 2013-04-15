package cs.java.json;

import static cs.java.lang.Lang.asDouble;
import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.json;
import static cs.java.lang.Lang.list;
import static cs.java.lang.Lang.no;

import java.util.Map;

import cs.java.collections.List;

public abstract class JSONDataBase implements JSONData {
	protected JSONObject data = json().createObject();;

	public JSONObject data() {
		return data;
	}

	public JSONArray getArray(String key) {
		return data.getArray(key);
	}

	public String getAsString(String key) {
		return data.get(key).getValue() + "";
	}

	public Boolean getBoolean(String key) {
		return data.getBoolean(key);
	}

	public Double getDouble(String key) {
		return data.getDouble(key);
	}

	public Integer getInteger(String key) {
		return data.getInteger(key);
	}

	public String getNumberString(String key) {
		return data.getNumber(key) + "";
	}

	public JSONObject getObject(String key) {
		return data.getObject(key);
	}

	public String getString(String key) {
		return data.getString(key);
	}

	@Override public void load(JSONObject data) {
		if (no(data)) return;
		this.data = data;
		onLoad(data);
	}

	public void loadStrings(List<String> list, JSONObject data, String id) {
		if (data.contains(id)) for (JSONType type : data.getArray(id))
			list.add(type.asString());
	}

	public List<String> loadStrings(String id) {
		if (!data.contains(id)) return null;
		List<String> stringlist = list();
		loadStrings(stringlist, data, id);
		return stringlist;
	}

	public <T extends JSONData> void put(String key, List<T> value) {
		data.put(key, json().create(value));
	}

	public <T extends JSONData> void put(String key, Map<String, T> value) {
		data.put(key, json().createJSONDataMap(value));
	}

	public <T extends JSONData> void putStringMap(String key, Map<String, String> value) {
		data.put(key, json().create(value));
	}

	@Override public final JSONObject save() {
		onSave(data);
		return data;
	}

	protected <T extends JSONData> T load(T data, JSONObject object) {
		if (no(object)) return null;
		data.load(object);
		return data;
	}

	protected <T extends JSONData> T load(T apiData, JSONObject jsonData, String id) {
		JSONObject object = jsonData.getObject(id);
		if (no(object)) return null;
		apiData.load(object);
		return apiData;
	}

	protected <T extends JSONData> T load(T apiData, String id) {
		return load(apiData, data, id);
	}

	protected void onLoad(JSONObject data) {
	}

	protected void onSave(JSONObject data) {
	}

	protected void put(String string, Number value) {
		data.put(string, value);
	}

	protected void put(String string, String value) {
		data.put(string, value);
	}

	protected void putNumberString(String string, String value) {
		put(string, asDouble(value));
	}

	protected void save(JSONObject object, String id, Double string) {
		if (is(string)) object.put(id, string);
	}

	protected void save(JSONObject object, String id, JSONDataBase data) {
		if (is(data)) object.put(id, data.save());
	}

	protected void save(JSONObject object, String id, String string) {
		if (is(string)) object.put(id, string);
	}
}
