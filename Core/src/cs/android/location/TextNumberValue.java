package cs.android.location;

import static cs.java.lang.Lang.unexpected;
import cs.java.json.JSONDataBase;
import cs.java.json.JSONObject;

public class TextNumberValue extends JSONDataBase {

	private Double value;
	private String text;

	public String getText() {
		return text;
	}

	public Double getValue() {
		return value;
	}

	@Override protected void onLoad(JSONObject data) {
		value = data.getDouble("value");
		text = data.getString("text");
	}

	@Override protected void onSave(JSONObject data) {
		throw unexpected();
	}
}
