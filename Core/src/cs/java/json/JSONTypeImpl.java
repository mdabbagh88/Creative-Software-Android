package cs.java.json;

import static cs.java.lang.Lang.is;

public class JSONTypeImpl implements JSONType {

	private final Object value;

	public JSONTypeImpl(Object value) {
		this.value = value;
	}

	@Override
	public JSONArray asArray() {
		return null;
	}

	@Override
	public Boolean asBoolean() {
		JSONBoolean json = asJSONBoolean();
		if (is(json)) return json.get();
		return null;
	}

	@Override
	public JSONContainer asContainer() {
		return null;
	}

	@Override
	public Double asDouble() {
		JSONNumber json = asJSONNumber();
		if (is(json)) {
			Number number = json.get();
			if (is(number)) return number.doubleValue();
		}
		return null;
	}

	@Override
	public JSONBoolean asJSONBoolean() {
		return null;
	}

	@Override
	public JSONNumber asJSONNumber() {
		return null;
	}

	@Override
	public JSONString asJSONString() {
		return null;
	}

	@Override
	public JSONObject asObject() {
		return null;
	}

	@Override
	public String asString() {
		JSONString jsonString = asJSONString();
		if (is(jsonString)) return jsonString.get();
		return null;
	}

	@Override
	public JSONValue<?> asValue() {
		return null;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public String toJSON() {
		return String.valueOf(getValue());
	}
}
