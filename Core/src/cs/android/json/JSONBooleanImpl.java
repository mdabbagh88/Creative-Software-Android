package cs.android.json;

import cs.java.json.JSONBoolean;
import cs.java.json.JSONTypeImpl;
import cs.java.json.JSONValue;

public class JSONBooleanImpl extends JSONTypeImpl implements JSONBoolean {

	private static JSONBooleanImpl TRUE = new JSONBooleanImpl(true);
	private static JSONBooleanImpl FALSE = new JSONBooleanImpl(false);

	public static JSONBoolean getInstance(boolean value) {
		if (value) return TRUE;
		return FALSE;
	}

	private final Boolean value;

	JSONBooleanImpl(Boolean value) {
		super(value);
		this.value = value;
	}

	@Override public JSONBoolean asJSONBoolean() {
		return this;
	}

	@Override public JSONValue<Boolean> asValue() {
		return this;
	}

	@Override
	public Boolean get() {
		return value;
	}
}
