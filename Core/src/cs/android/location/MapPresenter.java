package cs.android.location;

import static cs.java.lang.Lang.is;
import android.graphics.Bitmap;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

import cs.android.IActivityWidget;
import cs.android.viewbase.ActivityWidget;

public class MapPresenter extends ActivityWidget {

	private final int mapFrameId;
	private final HasMap hasMap;

	public MapPresenter(IActivityWidget parent, HasMap hasMap, int mapFrameId) {
		super(parent);
		this.hasMap = hasMap;
		this.mapFrameId = mapFrameId;
	}

	public MapView getMap() {
		return hasMap.getMap();
	}

	public GeoPoint getMapCenter() {
		return getMap().getMapCenter();
	}

	public void setMapCenter(GeoPoint geoPoint) {
		getMap().getController().setCenter(geoPoint);
	}

	public void setMapZoom(int zoom) {
		getMap().getController().setZoom(zoom);
	}

	@Override protected void onPause() {
		super.onPause();
		Bitmap drawingCache = getMap().getDrawingCache();
		if (is(drawingCache)) {
			ImageView view = new ImageView(context());
			view.setImageBitmap(Bitmap.createBitmap(drawingCache));
			getMapFrame().removeView(getMap());
			getMapFrame().addView(view);
		}
	}

	@Override protected void onResume() {
		super.onResume();
		getMap().setDrawingCacheEnabled(true);
		getMap().getOverlays().clear();
		getMapFrame().addView(getMap());
	}

	private FrameLayout getMapFrame() {
		return getFrameLayout(mapFrameId);
	}
}
