package cs.android.viewbase;

import static cs.java.lang.Lang.no;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import cs.java.lang.Value;

public abstract class ActivityBase extends Activity implements IsActivityBase {

	private ActivityManager manager;
	private ViewController presenter;

	@Override
	public Activity activity() {
		return this;
	}

	public int getScreenOrientation() {
		Display getOrient = getWindowManager().getDefaultDisplay();
		int orientation = Configuration.ORIENTATION_UNDEFINED;
		if (getOrient.getWidth() == getOrient.getHeight()) {
			orientation = Configuration.ORIENTATION_SQUARE;
		} else {
			if (getOrient.getWidth() < getOrient.getHeight()) {
				orientation = Configuration.ORIENTATION_PORTRAIT;
			} else {
				orientation = Configuration.ORIENTATION_LANDSCAPE;
			}
		}
		return orientation;
	}

	public void activityOnBackPressed() {
		super.onBackPressed();
	}

	@Override
	public Context context() {
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
		presenter = activityManager().create();
		activityManager().onCreate(state);
	}

	private ActivityManager activityManager() {
		if (no(manager)) manager = createActivityManager();
		return manager;
	}

	protected ActivityManager createActivityManager() {
		return new ActivityManager(this);
	}

	@Override public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override public Object onRetainNonConfigurationInstance() {
		return presenter;
	}

	@Override public void onSaveInstanceState(Bundle state) {
		presenter.onSaveInstanceState(state);
	}

	@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		presenter.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override protected void onDestroy() {
		super.onDestroy();
		activityManager().onDestroy();
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
		presenter.onStart();
		super.onStart();
	}

	@Override protected void onStop() {
		presenter.onStop();
		super.onStop();
	}
	
	@Override public boolean onCreateOptionsMenu(Menu menu) {
		return presenter.onCreateOptionsMenu(menu);
	}
	
	@Override public boolean onOptionsItemSelected(MenuItem item) {
		return presenter.onOptionsItemSelected(item);
	}

	@Override public boolean onPrepareOptionsMenu(Menu menu) {
		return presenter.onPrepareOptionsMenu(menu);
	}
}
