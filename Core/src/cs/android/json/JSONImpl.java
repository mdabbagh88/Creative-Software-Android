package cs.android.json;

import static cs.java.lang.Lang.unexpected;

import org.json.JSONTokener;

import cs.java.json.JSON;
import cs.java.json.JSONArray;
import cs.java.json.JSONBoolean;
import cs.java.json.JSONContainer;
import cs.java.json.JSONImplBase;
import cs.java.json.JSONNumber;
import cs.java.json.JSONObject;
import cs.java.json.JSONString;
import cs.java.json.JSONType;

public class JSONImpl extends JSONImplBase implements JSON {

	public static JSONType create(Object value) {
		if (value instanceof org.json.JSONObject)
			return new JSONObjectImpl((org.json.JSONObject) value);
		if (value instanceof org.json.JSONArray) return new JSONArrayImpl((org.json.JSONArray) value);
		if (value instanceof Boolean)
			return JSONBooleanImpl.getInstance(((Boolean) value).booleanValue());
		if (value instanceof String) return new JSONStringImpl((String) value);
		if (value instanceof Integer) return new JSONNumberImpl((Integer) value);
		if (value instanceof Long) return new JSONNumberImpl((Long) value);
		if (value instanceof Double) return new JSONNumberImpl((Double) value);
		if (value == org.json.JSONObject.NULL) return null;
		throw unexpected("some unexpected json value " + value);
	}

	@Override
	public JSONBoolean create(Boolean value) {
		return JSONBooleanImpl.getInstance(value);
	}

	@Override
	public JSONNumber create(Number number) {
		return new JSONNumberImpl(number);
	}

	@Override
	public JSONString create(String value) {
		return new JSONStringImpl(value);
	}

	@Override
	public JSONArray createArray() {
		return new JSONArrayImpl();
	}

	@Override
	public JSONObject createObject() {
		return new JSONObjectImpl();
	}

	@Override
	public JSONContainer parse(String json) {
		try {
			return create(new JSONTokener(json).nextValue()).asContainer();
		} catch (Exception e) {
			return null;
		}
	}

}
