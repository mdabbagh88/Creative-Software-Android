package cs.java.json;

import java.util.List;
import java.util.Map;


public interface JSON {
	JSONBoolean create(Boolean value);

	<T extends JSONData> JSONArray create(List<T> objects);

	<T extends JSONData> JSONObject createJSONDataMap(Map<String, T> value);

	JSONNumber create(Number value);

	JSONObject create(Map<String, String> value);

	JSONString create(String value);

	JSONArray createArray();

	JSONObject createObject();

	JSONContainer parse(String json);
}
