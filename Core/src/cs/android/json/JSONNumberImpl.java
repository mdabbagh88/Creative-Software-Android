package cs.android.json;

import cs.java.json.JSONNumber;
import cs.java.json.JSONTypeImpl;
import cs.java.json.JSONValue;

public class JSONNumberImpl extends JSONTypeImpl implements JSONNumber {

	private final Number value;

	public JSONNumberImpl(Number value) {
		super(value);
		this.value = value;
	}

	@Override public JSONNumber asJSONNumber() {
		return this;
	}

	@Override public JSONValue<?> asValue() {
		return this;
	}

	@Override
	public Number get() {
		return value;
	}
}
