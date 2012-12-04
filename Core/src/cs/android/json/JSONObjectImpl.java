package cs.android.json;

import static cs.java.lang.Lang.error;
import static cs.java.lang.Lang.is;

import java.util.Iterator;

import org.json.JSONException;

import cs.java.json.JSONContainer;
import cs.java.json.JSONObject;
import cs.java.json.JSONType;
import cs.java.json.JsonObjectImplBase;

public class JSONObjectImpl extends JsonObjectImplBase {

	private final org.json.JSONObject value;

	public JSONObjectImpl() {
		this(new org.json.JSONObject());
	}

	public JSONObjectImpl(org.json.JSONObject value) {
		super(value);
		this.value = value;
	}

	@Override public JSONContainer asContainer() {
		return this;
	}

	@Override public JSONObject asObject() {
		return this;
	}

	@Override public JSONType getImpl(String key) {
		Object valuekey = null;
		try {
			valuekey = value.get(key);
		} catch (Exception e) {
		}
		if (is(valuekey)) return JSONImpl.create(valuekey);
		return null;
	}

	@Override
	public int getSize() {
		return value.length();
	}

	@Override
	public Iterator<String> iterator() {
		return value.keys();
	}

	@Override
	public void put(String key, JSONType value) {
		try {
			this.value.put(key, value.getValue());
		} catch (JSONException e) {
			error(e);
		}
	}

	@Override
	public void remove(String key) {
		value.remove(key);
	}

}
