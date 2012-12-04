package cs.android.json;

import static cs.java.lang.Lang.error;

import java.util.Iterator;

import org.json.JSONException;

import cs.java.collections.GenericIterator;
import cs.java.json.JSONArray;
import cs.java.json.JSONContainer;
import cs.java.json.JSONType;
import cs.java.json.JsonArrayImplBase;

public class JSONArrayImpl extends JsonArrayImplBase implements JSONArray {

	private final org.json.JSONArray value;

	public JSONArrayImpl() {
		this(new org.json.JSONArray());
	}

	public JSONArrayImpl(org.json.JSONArray value) {
		super(value);
		this.value = value;
	}

	@Override
	public void add(JSONType value) {
		set(getSize(), value);
	}

	@Override public JSONArray asArray() {
		return this;
	}

	@Override public JSONContainer asContainer() {
		return this;
	}

	 @Override
	public JSONType get(int index) {
		try {
			return JSONImpl.create(value.get(index));
		} catch (JSONException e) {
			error(e);
		}
		return null;
	}

	@Override
	public int getSize() {
		return value.length();
	}

	@Override
	public Iterator<JSONType> iterator() {
		return new GenericIterator<JSONType>(getSize()) {
			@Override protected JSONType getValue() {
				return get(index());
			}
		};
	}

	@Override
	public void set(int index, JSONType type) {
		try {
			value.put(index, type.getValue());
		} catch (JSONException e) {
			error(e);
		}
	}

}
