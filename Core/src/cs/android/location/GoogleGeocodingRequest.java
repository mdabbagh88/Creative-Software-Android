package cs.android.location;

import static cs.java.lang.Lang.info;
import static cs.java.lang.Lang.json;
import static cs.java.lang.Lang.list;

import com.google.android.maps.GeoPoint;

import cs.android.lang.GetRequest;
import cs.java.collections.List;
import cs.java.json.JSONDataBase;
import cs.java.json.JSONObject;
import cs.java.json.JSONType;

public class GoogleGeocodingRequest extends GetRequest {

	public class AddressComponent extends JSONDataBase {
		public static final String STREET_NUMBER = "street_number";
		public static final String ROUTE = "route";
		public static final String LOCALITY = "locality";
		private String long_name;
		private String short_name;
		private final List<String> types = list();

		public String getLongName() {
			return long_name;
		}

		public String getShortName() {
			return short_name;
		}

		public List<String> getTypes() {
			return types;
		}

		@Override protected void onLoad(JSONObject data) {
			long_name = data.getString("long_name");
			short_name = data.getString("short_name");
			for (JSONType jsonType : data.getArray("types"))
				types.add(jsonType.asString());
		}
	}
	public class AddressResult extends JSONDataBase {

		private final List<String> types = list();
		private String formattedAddress;
		private final List<AddressComponent> addressComponents = list();
		private Location location;

		public AddressComponent getAddressComponent(String... types) {
			for (AddressComponent component : addressComponents)
				if (component.types.containsAll(list(types))) return component;
			return null;
		}

		public List<AddressComponent> getAddressComponents() {
			return addressComponents;
		}

		public String getFormattedAddress() {
			return formattedAddress;
		}

		public Location getLocation() {
			return location;
		}

		public List<String> getTypes() {
			return types;
		}

		@Override protected void onLoad(JSONObject data) {
			for (JSONType type : data.getArray("types"))
				types.add(type.asString());
			formattedAddress = data.getString("formatted_address");
			for (JSONType jsonType : data.getArray("address_components"))
				addressComponents.add(load(new AddressComponent(), jsonType.asObject()));
			location = load(new Location(), data.getObject("geometry"), "location");
		}
	}

	public class GoogleGeocodingResult extends JSONDataBase {

		public static final String STATUS_OK = "OK";

		private String status;
		private final List<AddressResult> results = list();

		public List<AddressResult> getResults() {
			return results;
		}

		public String getStatus() {
			return status;
		}

		@Override protected void onLoad(JSONObject data) {
			status = data.getString("status");
			for (JSONType resultsType : data.getArray("results"))
				results.add(load(new AddressResult(), resultsType.asObject()));
		}
	}

	public class Location extends JSONDataBase {
		private Double lat;
		private Double lng;

		public Double getLat() {
			return lat;
		}

		public Double getLng() {
			return lng;
		}

		@Override protected void onLoad(JSONObject data) {
			lat = data.getDouble("lat");
			lng = data.getDouble("lng");
		}
	}

	private static final String BASE_URL = "http://maps.googleapis.com/maps/api/geocode/json";

	private GoogleGeocodingResult result;

	public GoogleGeocodingRequest(GeoPoint point) {
		super(BASE_URL);
		add("latlng", point.getLatitudeE6() / 1E6 + "," + point.getLongitudeE6() / 1E6);
		add("sensor", "true");
	}

	public GoogleGeocodingRequest(String query) {
		super(BASE_URL);
		add("address", query);
		add("sensor", "true");
	}

	public GoogleGeocodingResult getResult() {
		return result;
	}

	@Override public void onSuccess() {
		super.onSuccess();
		JSONObject response = json(getResponse()).asObject();
		info(response.toJSON());
		result = new GoogleGeocodingResult();
		result.load(response);
	}

}

// {
// "status": "OK",
// "results": [ {
// "types": street_address,
// "formatted_address": "275-291 Bedford Ave, Brooklyn, NY 11211, USA",
// "address_components": [ {
// "long_name": "275-291",
// "short_name": "275-291",
// "types": street_number
// }, {
// "long_name": "Bedford Ave",
// "short_name": "Bedford Ave",
// "types": route
// }, {
// "long_name": "New York",
// "short_name": "New York",
// "types": [ "locality", "political" ]
// }, {
// "long_name": "Brooklyn",
// "short_name": "Brooklyn",
// "types": [ "administrative_area_level_3", "political" ]
// }, {
// "long_name": "Kings",
// "short_name": "Kings",
// "types": [ "administrative_area_level_2", "political" ]
// }, {
// "long_name": "New York",
// "short_name": "NY",
// "types": [ "administrative_area_level_1", "political" ]
// }, {
// "long_name": "United States",
// "short_name": "US",
// "types": [ "country", "political" ]
// }, {
// "long_name": "11211",
// "short_name": "11211",
// "types": postal_code
// } ],
// "geometry": {
// "location": {
// "lat": 40.7142298,
// "lng": -73.9614669
// },
// "location_type": "RANGE_INTERPOLATED",
// "viewport": {
// "southwest": {
// "lat": 40.7110822,
// "lng": -73.9646145
// },
// "northeast": {
// "lat": 40.7173774,
// "lng": -73.9583193
// }
// }
// }
// },
//
// ... Additional results[] ...
