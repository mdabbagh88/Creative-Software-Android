package cs.android.location;

import static cs.java.lang.Lang.unexpected;
import cs.java.json.JSONArray;
import cs.java.json.JSONDataBase;
import cs.java.json.JSONObject;

public class GoogleDirections extends JSONDataBase {

	class GoogleRoute extends JSONDataBase {
		@Override protected void onLoad(JSONObject data) {
		}

		@Override protected void onSave(JSONObject data) {
			throw unexpected();
		}

	}
	public static String STATUS_OK = "OK";

	public static String STATUS_ZERO_RESULTS = "ZERO_RESULTS";
	private TextNumberValue duration;
	private TextNumberValue distance;

	private String status;

	public TextNumberValue getDistance() {
		return distance;
	}

	public TextNumberValue getDuration() {
		return duration;
	}

	public String getStatus() {
		return status;
	}

	@Override protected void onLoad(JSONObject data) {
		status = data.getString("status");
		JSONArray routes = data.getArray("routes");
		if (routes.getSize() > 0) {
			JSONObject route0legs = routes.getObject(0).getArray("legs").getObject(0);
			duration = load(new TextNumberValue(), route0legs, "duration");
			distance = load(new TextNumberValue(), route0legs, "distance");
		}
	}

	@Override protected void onSave(JSONObject data) {
		throw unexpected();
	}

}
