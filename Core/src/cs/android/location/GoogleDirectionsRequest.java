//package cs.android.location;
//
//import static cs.java.lang.Lang.json;
//
//import com.google.android.maps.GeoPoint;
//
//import cs.android.lang.GetRequest;
//import cs.java.json.JSONObject;
//
//public class GoogleDirectionsRequest extends GetRequest {
//
//	private GoogleDirections directions;
//
//	public GoogleDirectionsRequest(GeoPoint origin, GeoPoint target) {
//		super("http://maps.googleapis.com/maps/api/directions/json");
//		add("origin", asString(origin));
//		add("destination", asString(target));
//		add("units", "metric");
//		add("sensor", "true");
//	}
//
//	public GoogleDirections getDirections() {
//		return directions;
//	}
//
//	@Override public void onSuccess() {
//		super.onSuccess();
//		JSONObject jsonData = json(getResponse()).asObject();
//		directions = new GoogleDirections();
//		directions.load(jsonData);
//	}
//
//	private String asString(GeoPoint point) {
//		return point.getLatitudeE6() / 1E6 + "," + point.getLongitudeE6() / 1E6;
//	}
//
//}
