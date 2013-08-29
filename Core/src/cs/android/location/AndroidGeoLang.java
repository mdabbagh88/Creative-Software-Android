package cs.android.location;

import static cs.java.lang.Lang.to1E6;
import static cs.java.lang.Lang.no;
import android.location.Address;
import android.location.Location;

import com.google.android.maps.GeoPoint;

public class AndroidGeoLang {
	public static GeoPoint toGeoPoint(Address value) {
		if (no(value)) return null;
		return new GeoPoint(to1E6(value.getLatitude()), to1E6(value.getLongitude()));
	}

	public static GeoPoint toGeoPoint(Location value) {
		if (no(value)) return null;
		return new GeoPoint(to1E6(value.getLatitude()), to1E6(value.getLongitude()));
	}
}
