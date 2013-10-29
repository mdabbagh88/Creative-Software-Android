package cs.android.viewbase;

import static cs.java.lang.Lang.no;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import cs.java.lang.Value;

public abstract class ActivityBase extends ActionBarActivity implements CSActivity {

	private ActivityManager manager;
	private ViewController presenter;

	@Override public Activity activity() {
		return this;
	}

	public void activityOnBackPressed() {
		super.onBackPressed();
	}

	@Override public Context context() {
		return this;
	}

	@Override public ViewController getPresenter() {
		return presenter;
	}

	@Override public Object getSavedInstance() {
		return getLastCustomNonConfigurationInstance();
	}

	public MenuInflater getSupportMenuInflater() {
		return getMenuInflater();
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

	public boolean onCreateOptionsMenu(Menu menu) {
		OnMenu onMenu = new OnMenu(menu);
		presenter.onCreateOptionsMenu(onMenu);
		return onMenu.result.get();
	}

	@Override public boolean onKeyDown(int keyCode, KeyEvent event) {
		OnKeyDownResult onKeyDown = new OnKeyDownResult(keyCode, event);
		presenter.onKeyDown(onKeyDown);
		return super.onKeyDown(keyCode, event);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		OnMenuItem onMenuItem = new OnMenuItem(item);
		presenter.onOptionsItemSelected(onMenuItem);
		return onMenuItem.result.get();
	}

	public boolean onPrepareOptionsMenu(Menu menu) {
		OnMenu onMenu = new OnMenu(menu);
		presenter.onPrepareOptionsMenu(onMenu);
		return onMenu.result.get();
	}

	@Override public Object onRetainCustomNonConfigurationInstance() {
		return presenter;
	}

	@Override public void onSaveInstanceState(Bundle state) {
		presenter.onSaveInstanceState(state);
	}

	private ActivityManager activityManager() {
		if (no(manager)) manager = createActivityManager();
		return manager;
	}

	protected ActivityManager createActivityManager() {
		return new ActivityManager(this);
	}

	@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		presenter.onActivityResult(new ActivityResult(requestCode, resultCode, data));
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

}
