package cs.android.location;

import static cs.java.lang.Lang.event;
import static cs.android.location.AndroidGeoLang.toGeoPoint;
import static cs.java.lang.Lang.MINUTE;
import static cs.java.lang.Lang.fire;
import static cs.java.lang.Lang.info;
import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.no;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;

import cs.android.viewbase.ContextPresenter;
import cs.java.event.Event;

public class MyLocation extends ContextPresenter {

	private class MyLocationListener implements LocationListener {
		@Override
		public void onLocationChanged(Location location) {
			MyLocation.this.onLocationChanged(location);
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

	}
	private static final int NET_OR_GPS_MINUTES = 15;
	private static final int UPDATE_DISTANCE = 50;

	private static final int UPDATE_TIME = 15 * MINUTE;
	private final Event<Location> onLocationEvent = event();
	private Location gpsLocation;
	private Location networkLocation;

	private Location lastLocation;

	private final MyLocationListener listener = new MyLocationListener();

	public Location getLastLocation() {
		return lastLocation;
	}

	public GeoPoint getLastPoint() {
		return toGeoPoint(getLastLocation());
	}

	public Event<Location> getOnLocation() {
		return onLocationEvent;
	}

	public void startUpdates() {
		requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_TIME, UPDATE_DISTANCE, listener);
		requestLocationUpdates(LocationManager.NETWORK_PROVIDER, UPDATE_TIME, UPDATE_DISTANCE, listener);
		updateLocation();
	}

	public void stopUpdates() {
		getLocationManager().removeUpdates(listener);
	}

	public void updateLocation() {
		updateLocation(getLocationManager().getLastKnownLocation(LocationManager.GPS_PROVIDER));
		updateLocation(getLocationManager().getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
	}

	private Location chooseLocation() {
		if (no(gpsLocation)) {
			if (is(networkLocation)) return networkLocation;
			return null;
		} else if (no(networkLocation)) return gpsLocation;

		if (networkLocation.getTime() - gpsLocation.getTime() > NET_OR_GPS_MINUTES * MINUTE)
			return networkLocation;
		return gpsLocation;
	}

	private void onLocationChanged(Location location) {
		updateLocation(location);
	}

	private void requestLocationUpdates(String provider, int updateTime, int updateDistance,
			MyLocationListener listener) {
		getLocationManager().requestLocationUpdates(provider, updateTime, updateDistance, listener);
	}

	private void updateLocation(Location location) {
		if (is(location)) if (location.getProvider() == LocationManager.GPS_PROVIDER)
			gpsLocation = location;
		else networkLocation = location;

		Location newLocation = chooseLocation();
		if (newLocation != lastLocation || no(newLocation)) {
			lastLocation = newLocation;
			info(location);
			fire(onLocationEvent, newLocation);
		}
	}

}