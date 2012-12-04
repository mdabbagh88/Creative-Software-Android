package cs.java.json;

public class JSONNullImpl extends JSONTypeImpl implements JSONString {

	public JSONNullImpl() {
		super(null);
	}

	@Override public JSONValue<?> asValue() {
		return this;
	}

	@Override
	public String get() {
		return null;
	}

}
