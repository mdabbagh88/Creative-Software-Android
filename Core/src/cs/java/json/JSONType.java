package cs.java.json;

public interface JSONType {
	JSONArray asArray();

	Boolean asBoolean();

	JSONContainer asContainer();

	Double asDouble();

	JSONBoolean asJSONBoolean();

	JSONNumber asJSONNumber();

	JSONString asJSONString();

	JSONObject asObject();

	String asString();

	JSONValue<?> asValue();

	Object getValue();

	String toJSON();
}
