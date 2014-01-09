package cs.java.json;

import java.util.List;
import java.util.Map;

public interface JSON {
	JSONBoolean create(Boolean value);

	<T extends JSONData> JSONArray create(List<T> objects);

	JSONObject create(Map<String, String> value);

	JSONNumber create(Number value);

	JSONString create(String value);

	JSONArray createArray();

	<T extends JSONData> JSONObject createJSONDataMap(Map<String, T> value);

	JSONObject createObject();

	JSONContainer parse(String json);
}
