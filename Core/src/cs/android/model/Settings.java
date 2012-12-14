package cs.android.model;

import static cs.android.lang.AndroidLang.invoke;
import static cs.android.lang.AndroidLang.respondsTo;
import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.iterate;
import static cs.java.lang.Lang.json;
import static cs.java.lang.Lang.map;
import static cs.java.lang.Lang.no;

import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import cs.android.lang.CSAsyncTask;
import cs.android.viewbase.ContextPresenter;
import cs.java.collections.List;
import cs.java.collections.MapItem;
import cs.java.json.JSONArray;
import cs.java.json.JSONContainer;
import cs.java.json.JSONData;
import cs.java.json.JSONObject;

@SuppressLint("CommitPrefEdits")
public class Settings extends ContextPresenter {

	private static Settings instance = new Settings();

	public static Settings get() {
		return instance;
	}

	private final SharedPreferences preferences;

	public Settings() {
		preferences = getPreferences("settings");
	}

	public Settings(String name) {
		preferences = getPreferences(name);
	}

	public void clear() {
		Editor editor = preferences.edit();
		editor.clear();
		save(editor);
	}

	public void clear(String key) {
		Editor editor = preferences.edit();
		editor.remove(key);
		save(editor);
	}

	protected SharedPreferences getPreferences(String key) {
		return context().getSharedPreferences(key, Context.MODE_PRIVATE);
	}

	public boolean has(String key) {
		return preferences.contains(key);
	}

	public <T extends JSONData> T load(T data, String key) {
		String loadString = loadString(key);
		if (no(loadString)) return null;
		JSONObject json = json(loadString).asObject();
		if (no(json)) return null;
		data.load(json);
		return data;
	}

	public JSONArray loadArray(String key) {
		JSONContainer container = loadJSONContainer(key);
		return is(container) ? container.asArray() : null;
	}

	public boolean loadBoolean(String key) {
		return loadBoolean(key, false);
	}

	public boolean loadBoolean(String key, boolean defaultValue) {
		return preferences.getBoolean(key, defaultValue);
	}

	public Integer loadInteger(String key) {
		return preferences.getInt(key, 0);

	}

	public Integer loadInteger(String key, int defaultValue) {
		return preferences.getInt(key, defaultValue);
	}

	private JSONContainer loadJSONContainer(String key) {
		String loadString = loadString(key);
		if (no(loadString)) return null;
		JSONContainer parsed = json().parse(loadString);
		return parsed;
	}

	public JSONObject loadObject(String key) {
		JSONContainer container = loadJSONContainer(key);
		return is(container) ? container.asObject() : null;
	}

	public String loadString(String key) {
		return preferences.getString(key, null);
	}

	private void save(final Editor editor) {
		if (respondsTo(editor, "apply"))
			invoke(editor, "apply");
		else new CSAsyncTask() {
			@Override public void run() {
				editor.commit();
			}
		};
	}

	protected void save(String... keysvalues) {
		Editor editor = preferences.edit();
		for (MapItem<String, String> keyvalue : iterate(map((Object[]) keysvalues)))
			editor.putString(keyvalue.key(), keyvalue.value());
		save(editor);
	}

	public void save(String key, Boolean value) {
		if (no(value))
			clear(key);
		else {
			Editor editor = preferences.edit();
			editor.putBoolean(key, value);
			save(editor);
		}
	}

	public void save(String key, Integer value) {
		if (no(value))
			clear(key);
		else {
			Editor editor = preferences.edit();
			editor.putInt(key, value);
			save(editor);
		}
	}

	public void save(String key, JSONData data) {
		if (no(data))
			clear(key);
		else save(key, data.save().toJSON());
	}

	public <T extends JSONData> void save(String key, List<T> data) {
		if (no(data))
			clear(key);
		else save(key, json().create(data).toJSON());
	}

	public <T extends JSONData> void save(String key, Map<String, T> data) {
		if (no(data))
			clear(key);
		else save(key, json().createJSONDataMap(data).toJSON());
	}

	public void save(String key, String value) {
		if (no(value)) clear(key);
		Editor editor = preferences.edit();
		editor.putString(key, value);
		save(editor);
	}
}
