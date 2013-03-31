package cs.android.location;

import static cs.java.lang.Lang.is;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import cs.android.IActivityWidget;
import cs.android.viewbase.ViewController;
import cs.java.lang.Run;

public class MyLocationOverlayPresenter extends ViewController {

	private MyLocationOverlay myLocationOverLay;
	private final HasMap hasMap;
	private MapView map;

	public MyLocationOverlayPresenter(IActivityWidget parent, HasMap hasMap) {
		super(parent);
		this.hasMap = hasMap;
	}

	public void centerToMyLocation() {
		if (is(myLocationOverLay.getMyLocation()))
			map.getController().setCenter(myLocationOverLay.getMyLocation());
	}

	@Override public void onPause() {
		super.onPause();
		myLocationOverLay.disableCompass();
		myLocationOverLay.disableMyLocation();
	}

	@Override protected void onResume() {
		super.onResume();
		map = hasMap.getMap();
		myLocationOverLay = new MyLocationOverlay(context(), map);
		myLocationOverLay.enableCompass();
		myLocationOverLay.enableMyLocation();
		map.getOverlays().add(myLocationOverLay);
		myLocationOverLay.runOnFirstFix(new Run() {
			@Override
			public void run() {
				centerToMyLocation();
			}
		});
	}

}
