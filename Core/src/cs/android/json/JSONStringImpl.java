package cs.android.json;

import cs.java.json.JSONString;
import cs.java.json.JSONTypeImpl;
import cs.java.json.JSONValue;

public class JSONStringImpl extends JSONTypeImpl implements JSONString {

	private final String value;

	public JSONStringImpl(String value) {
		super(value);
		this.value = value;
	}

	@Override public JSONString asJSONString() {
		return this;
	}

	@Override public JSONValue<?> asValue() {
		return this;
	}

	@Override
	public String get() {
		return value;
	}

}
