package cs.android.viewbase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.maps.MapActivity;

import cs.java.lang.Value;

public abstract class MapActivityBase extends MapActivity implements IsActivityBase {

	private final ActivityManager manager = new ActivityManager(this);
	private ViewController presenter;

	public void activityOnBackPressed() {
		super.onBackPressed();
	}

	 @Override
	public Context context() {
		return this;
	}

	public Activity getActivity() {
		return this;
	}

	 @Override
	public ViewController getPresenter() {
		return presenter;
	}

	@Override public void onBackPressed() {
		Value<Boolean> willPressBack = new Value<Boolean>(true);
		presenter.onBackPressed(willPressBack);
		if (willPressBack.get()) super.onBackPressed();
	}

	@Override public void onCreate(Bundle state) {
		super.onCreate(state);
		presenter = manager.create();
		manager.onCreate(state);
	}

	@Override public Object onRetainNonConfigurationInstance() {
		return presenter;
	}

	@Override public void onSaveInstanceState(Bundle state) {
		presenter.onSaveInstanceState(state);
	}

	@Override protected boolean isRouteDisplayed() {
		return false;
	}

	@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		presenter.onActivityResult(requestCode, resultCode, data);
	}

	@Override protected void onDestroy() {
		super.onDestroy();
		manager.onDestroy();
	}

	@Override protected void onPause() {
		presenter.onPause();
		super.onPause();
	}

	@Override protected void onResume() {
		presenter.onResume();
		super.onResume();
	}

	@Override protected void onStart() {
		// presenter.onStart();
		super.onStart();
	}

	@Override protected void onStop() {
		// presenter.onStop();
		super.onStop();
	}
}
