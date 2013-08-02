package cs.android.location;

import static cs.android.lang.AndroidLang.getDrawable;
import static cs.java.lang.Lang.is;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import cs.android.viewbase.ViewController;

public class ImageMapOverlay extends ItemizedOverlay<OverlayItem> {

	private OverlayItem overlay;
	private String title = "";
	private String snipet = "";

	public ImageMapOverlay(ViewController activity, int drawable) {
		super(boundCenterBottom(getDrawable(activity, drawable)));
	}

	public void setPoint(GeoPoint point) {
		overlay = new OverlayItem(point, title, snipet);
		populate();
	}

	public void setSnipet(String snipet) {
		this.snipet = snipet;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override public int size() {
		if (is(overlay)) return 1;
		return 0;
	}

	@Override protected OverlayItem createItem(int i) {
		return overlay;
	}

}
