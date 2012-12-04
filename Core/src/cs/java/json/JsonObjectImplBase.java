package cs.java.json;

import cs.java.collections.Map;

import static cs.java.lang.Lang.*;

public abstract class JsonObjectImplBase extends JSONTypeImpl implements JSONObject {
	public JsonObjectImplBase(Object value) {
		super(value);
	}

	@Override
	public <T> Map<String, T> asMap(Class<T> valueType) {
		Map<String, T> value = map();
		for (String key : this)
			value.put(key, (T) get(key).getValue());
		return value;
	}

	@Override
	public boolean contains(String key) {
		return is(get(key));
	}

	@Override
	public JSONType get(String key) {
		return getImpl(key);
	}

	@Override
	public JSONArray getArray(String key) {
		JSONType value = get(key);
		if (is(value)) {
			JSONArray typeValue = value.asArray();
			if (is(typeValue)) return typeValue;
			throw exception("Expected JSONObject, found ", value.getValue());
		}
		return null;
	}

	@Override
	public Boolean getBoolean(String key) {
		JSONType value = get(key);
		if (is(value)) {
			JSONBoolean typeValue = value.asJSONBoolean();
			if (is(typeValue)) return typeValue.get();
			throw exception("Expected Boolean, found ", value.getValue());
		}
		return null;
	}

	protected abstract JSONType getImpl(String key);

	@Override
	public Number getNumber(String key) {
		JSONType value = get(key);
		if (is(value)) {
			JSONNumber typevalue = value.asJSONNumber();
			if (is(typevalue)) return typevalue.get();
			throw exception("Expected Number, found ", value.getValue());
		}
		return null;
	}

	@Override
	public Double getDouble(String key) {
		Number value = getNumber(key);
		if (is(value)) return value.doubleValue();
		return null;
	}

	@Override
	public Integer getInteger(String key) {
		Number value = getNumber(key);
		if (is(value)) return value.intValue();
		return null;
	}

	@Override
	public Long getLong(String key) {
		Number value = getNumber(key);
		if (is(value)) return value.longValue();
		return null;
	}

	@Override
	public JSONObject getObject(String key) {
		JSONType value = get(key);
		if (is(value)) {
			JSONObject typevalue = value.asObject();
			if (is(typevalue)) return typevalue;
			throw exception("Expected JSONObject, found ", value.getValue());
		}
		return null;
	}

	@Override
	public String getString(String key) {
		JSONType value = get(key);
		if (is(value)) {
			JSONString typevalue = value.asJSONString();
			if (is(typevalue)) return typevalue.get();
			throw exception("Expected String, found ", value.getValue());
		}
		return null;
	}

	@Override
	public void put(String key, Boolean value) {
		put(key, json().create(value));
	}

	@Override
	public void put(String key, Number value) {
		put(key, json().create(value));
	}

	@Override
	public void put(String key, String value) {
		put(key, json().create(value));
	}
}