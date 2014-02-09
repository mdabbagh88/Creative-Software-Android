package cs.java.json;

import static cs.java.lang.Lang.iterate;

import java.util.List;
import java.util.Map;

import cs.java.collections.Mapped;

public abstract class JSONImplBase implements JSON {

	@Override public <T extends JSONData> JSONArray create(List<T> objects) {
		JSONArray array = createArray();
		for (T jsonData : objects)
			array.add(jsonData.save());
		return array;
	}

	@Override public JSONObject create(Map<String, String> value) {
		JSONObject object = createObject();
		for (Mapped<String, String> item : iterate(value))
			object.put(item.key(), item.value());
		return object;
	}

	@Override public <T extends JSONData> JSONObject createJSONDataMap(Map<String, T> value) {
		JSONObject object = createObject();
		for (Mapped<String, T> item : iterate(value))
			object.put(item.key(), item.value().save());
		return object;
	}

}
