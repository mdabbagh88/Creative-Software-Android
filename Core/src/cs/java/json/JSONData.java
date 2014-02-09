package cs.java.json;

import static cs.java.lang.Lang.asDouble;
import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.iterate;
import static cs.java.lang.Lang.json;
import static cs.java.lang.Lang.list;
import static cs.java.lang.Lang.newInstance;
import static cs.java.lang.Lang.no;

import java.util.Iterator;
import java.util.Map;

import cs.java.collections.Iteration;
import cs.java.collections.List;
import cs.java.lang.Factory;

public class JSONData implements Iterable<String> {

	private JSONObject _data;

	private int _index;

	public JSONData() {
		_data = json().createObject();
	}

	public JSONData(JSONObject object) {
		load(object);
	}

	protected <T extends JSONData> List createList(final Class<T> type, String id) {
		return createList(new Factory<T>() {
			public T create() {
				return newInstance(type);
			}
		}, id);
	}

	protected <T extends JSONData> List createList(Factory<T> factory, String id) {
		List list = list();
		Iteration<JSONType> iterate = iterate(getArray(id));
		for (JSONType type : iterate) {
			T item = load(factory.create(), type.asObject());
			item.index(iterate.index());
			list.add(item);
		}
		return list;
	}

	public JSONObject get(String key) {
		return _data.getObject(key);
	}

	public JSONArray getArray(String key) {
		return _data.getArray(key);
	}

	public Boolean getBoolean(String key) {
		return _data.getBoolean(key);
	}

	public JSONData getData(String key) {
		return new JSONData(_data.getObject(key));
	}

	public Double getDouble(String key) {
		try {
			return Double.parseDouble(getString(key));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public Integer getInteger(String key) {
		try {
			return Integer.parseInt(getString(key));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public String getString(String key) {
		JSONType jsonType = _data.get(key);
		return is(jsonType) ? jsonType.getValue() + "" : "";
	}

	public int index() {
		return _index;
	}

	public void index(int index) {
		_index = index;
	}

	public Iterator<String> iterator() {
		return _data.iterator();
	}

	public void load(JSONData data) {
		load(data.object());
	}

	public void load(JSONObject data) {
		if (no(data)) return;
		_data = data;
		onLoad(data);
	}

	protected <T extends JSONData> T load(T apiData, JSONData jsonData, String id) {
		return load(apiData, jsonData._data, id);
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
		return load(apiData, _data, id);
	}

	public void loadStrings(List<String> list, JSONObject data, String id) {
		if (data.contains(id)) for (JSONType type : data.getArray(id))
			list.add(type.asString());
	}

	public List<String> loadStrings(String id) {
		if (!_data.contains(id)) return null;
		List<String> stringlist = list();
		loadStrings(stringlist, _data, id);
		return stringlist;
	}

	public JSONObject object() {
		return _data;
	}

	protected void onLoad(JSONObject data) {
	}

	protected void onSave(JSONObject data) {
	}

	public void put(String key, Boolean value) {
		_data.put(key, value);
	}

	public <T extends JSONData> void put(String key, List<T> value) {
		_data.put(key, json().create(value));
	}

	public <T extends JSONData> void put(String key, Map<String, T> value) {
		_data.put(key, json().createJSONDataMap(value));
	}

	public void put(String key, Number value) {
		_data.put(key, value);
	}

	public void put(String key, String value) {
		_data.put(key, value);
	}

	public void putNumberString(String string, String value) {
		put(string, asDouble(value));
	}

	public <T extends JSONData> void putStringMap(String key, Map<String, String> value) {
		_data.put(key, json().create(value));
	}

	public final JSONObject save() {
		onSave(_data);
		return _data;
	}

	protected void save(JSONObject object, String id, Double string) {
		if (is(string)) object.put(id, string);
	}

	protected void save(JSONObject object, String id, JSONData data) {
		if (is(data)) object.put(id, data.save());
	}

	protected void save(JSONObject object, String id, String string) {
		if (is(string)) object.put(id, string);
	}
}
