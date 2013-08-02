package cs.android.viewbase;

import static cs.android.lang.AndroidLang.INVOKE_FAILED;
import static cs.android.lang.AndroidLang.event;
import static cs.android.lang.AndroidLang.invoke;
import static cs.java.lang.Lang.No;
import static cs.java.lang.Lang.fire;
import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.no;
import static cs.java.lang.Lang.set;
import static cs.java.lang.Lang.unexpected;

import java.io.Serializable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.view.View;
import android.view.WindowManager.LayoutParams;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuInflater;

import cs.java.event.Event;
import cs.java.event.Task;
import cs.java.lang.Value;

public abstract class ViewController extends Widget<View> {

	protected Activity _activity;
	private Bundle state;
	public final Event<Void> onPause = event();
	public final Event<Bundle> onCreate = event();
	public final Event<Value<Boolean>> onBack = event();
	public final Event<Void> onStart = event();
	public final Event<Void> onStop = event();
	public final Event<Bundle> onSaveInstance = event();
	public final Event<Void> onDestroy = event();
	public final Event<Void> onResume = event();
	public final Event<OnMenu> onPrepareOptionsMenu = event();
	public final Event<OnMenuItem> onOptionsItemSelected = event();
	public final Event<OnMenu> onCreateOptionsMenu = event();
	public final Event<ActivityResult> onActivityResult = event();
	private Task parentEventsTask;
	private boolean created;
	private boolean resumed;
	private boolean paused;
	private boolean destroyed;
	protected final ViewController _parent;
	private int viewId;
	private LayoutId layoutId;
	public static ViewController root;

	public ViewController(ViewController parent) {
		this._parent = parent;
		listenParent();
	}

	public FragmentManager getSupportFragmentManager() {
		Object manager = invoke(activity(), "getSupportFragmentManager");
		if (manager == INVOKE_FAILED) return null;
		return (FragmentManager) manager;
	}

	public ViewController(ViewController parent, int viewId) {
		this._parent = parent;
		this.viewId = viewId;
		listenParent();
	}

	@SuppressWarnings("deprecation") public int getScreenOrientation() {
		Display getOrient = activity().getWindowManager().getDefaultDisplay();
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

	public ViewController(ViewController parent, LayoutId id) {
		this._parent = parent;
		layoutId = id;
		listenParent();
	}

	public ViewController(LayoutId layoutId) {
		this.layoutId = layoutId;
		_parent = null;
		root = this;
		listenParent();
	}

	public Activity activity() {
		return _activity;
	}

	public View asView() {
		if (is(getView())) return getView();
		else if (set(viewId)) {
			setView(_parent.asView().findViewById(viewId));
			if (no(getView())) throw unexpected("Expected", this, "in parent", _parent);
		} else if (set(layoutId)) setView(inflateLayout(layoutId.value));
		else setView(_parent.asView());
		return getView();
	}

	public Bundle getState() {
		return state;
	}

	public int getViewId() {
		return viewId;
	}

	public Context context() {
		if (is(_parent)) return _parent.context();
		return super.context();
	}

	public boolean isCreated() {
		return created;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public boolean isPaused() {
		return paused;
	}

	public boolean isResumed() {
		return resumed;
	}

	public void onBackPressed(Value<Boolean> goBack) {
		fire(onBack, goBack);
	}

	public void onDeinitialize(Bundle state) {
		parentEventsTask.cancel();
		onPause();
		onSaveInstanceState(state);
		onStop();
	}

	public void onInitialize(Bundle state) {
		onCreate(state);
		onStart();
		onResume();
	}

	protected void setActivity(Activity activity) {
		if (paused) setView(null);
		_activity = activity;
		setContext(activity);
	}

	protected Bundle getIntentExtras() {
		return activity().getIntent().getExtras();
	}

	public void goBack() {
		if (is(_parent)) _parent.goBack();
		else activity().onBackPressed();
	}

	protected boolean isFirstTime() {
		return no(state);
	}

	protected boolean isMainActionRun() {
		return Intent.ACTION_MAIN.equals(activity().getIntent().getAction());
	}

	protected void onActivityResult(ActivityResult result) {
		fire(onActivityResult, result);
	}

	/**
	 * initialize view
	 */
	protected void onCreate() {
	}

	protected void onCreate(Bundle state) {
		if (is(_parent)) setActivity(_parent.activity());

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			activity().getWindow().setFlags(LayoutParams.FLAG_HARDWARE_ACCELERATED,
					LayoutParams.FLAG_HARDWARE_ACCELERATED);
		// overridePendingTransition(R.anim.right_to_center, R.anim.center_to_left);

		this.state = state;
		fire(onCreate, state);
		onCreate();
		if (no(state)) onCreateFirstTime();
		else onCreateRestore(state);
		created = true;
		paused = No;
	}

	/**
	 * initialize view
	 */
	protected void onCreateFirstTime() {
	}

	/**
	 * initialize view
	 */
	protected void onCreateRestore(Bundle state) {
	}

	public void onDestroy() {
		destroyed = true;
		setActivity(null);
		setView(null);
		fire(onDestroy);
	}

	protected void onPause() {
		resumed = false;
		paused = true;
		fire(onPause);
	}

	/**
	 * update view
	 */
	protected void onResume() {
		resumed = true;
		paused = false;
		fire(onResume);
	}

	protected void onSaveInstanceState(Bundle state) {
		fire(onSaveInstance, state);
	}

	protected void onStart() {
		resumed = true;
		paused = false;
		fire(onStart);
	}

	protected void onStop() {
		resumed = false;
		state = null;
		fire(onStop);
	}

	protected void overridePendingTransition(int in, int out) {
		activity().overridePendingTransition(in, out);
	}

	protected void setContentView(int layoutResId) {
		activity().setContentView(layoutResId);
	}

	public void startActivity(Class<? extends Activity> activityClass) {
		startActivity(new Intent(activity(), activityClass));
	}

	public void startActivity(Intent intent) {
		activity().startActivity(intent);
	}

	public void startActivityForResult(Class<? extends Activity> activityClass, int requestCode) {
		startActivityForResult(new Intent(activity(), activityClass), requestCode);
	}

	public void startActivityForResult(Intent intent, int requestCode) {
		activity().startActivityForResult(intent, requestCode);
	}

	public void switchActivity(Class<? extends Activity> activityClass) {
		switchActivity(new Intent(activity(), activityClass));
	}

	public void switchActivity(Class<? extends Activity> activityClass, String key, Serializable value) {
		Intent intent = new Intent(activity(), activityClass);
		intent.putExtra(key, value);
		switchActivity(intent);
	}

	public void switchActivity(Class<? extends Activity> activityClass, int resultCode) {
		activity().setResult(resultCode);
		switchActivity(new Intent(activity(), activityClass));
	}

	public void switchActivity(Intent intent) {
		startActivity(intent);
		activity().finish();
	}

	public void listenParent() {
		if (is(_parent))
			parentEventsTask = new Task(_parent.onCreate, _parent.onStart, _parent.onResume,
					_parent.onPause, _parent.onStop, _parent.onSaveInstance, _parent.onDestroy,
					_parent.onBack, _parent.onActivityResult, _parent.onCreateOptionsMenu,
					_parent.onOptionsItemSelected, _parent.onPrepareOptionsMenu) {
				@SuppressWarnings("unchecked") public void run() {
					if (event == _parent.onCreate) onCreate((Bundle) argument);
					if (event == _parent.onStart) onStart();
					if (event == _parent.onResume) onResume();
					if (event == _parent.onPause) onPause();
					if (event == _parent.onStop) onStop();
					if (event == _parent.onSaveInstance) onSaveInstanceState((Bundle) argument);
					if (event == _parent.onDestroy) onDestroy();
					if (event == _parent.onBack) onBackPressed((Value<Boolean>) argument);
					if (event == _parent.onActivityResult) onActivityResult((ActivityResult) argument);
					if (event == _parent.onCreateOptionsMenu) onCreateOptionsMenu((OnMenu) argument);
					if (event == _parent.onOptionsItemSelected) onOptionsItemSelected((OnMenuItem) argument);
					if (event == _parent.onPrepareOptionsMenu) onPrepareOptionsMenu((OnMenu) argument);
				}
			};
	}

	public void onCreateOptionsMenu(OnMenu menu) {
		fire(onCreateOptionsMenu, menu);
	}

	public void onOptionsItemSelected(OnMenuItem item) {
		fire(onOptionsItemSelected, item);
	}

	public void onPrepareOptionsMenu(OnMenu menu) {
		fire(onPrepareOptionsMenu, menu);
	}

	public FragmentManager getFragmentManager() {
		return ((CSActivity) activity()).getSupportFragmentManager();
	}

	public ActionBar getActionBar() {
		return ((CSActivity) activity()).getSupportActionBar();
	}

	public MenuInflater getMenuInflater() {
		return ((CSActivity) activity()).getSupportMenuInflater();
	}

	public void invalidateOptionsMenu() {
		((CSActivity) activity()).supportInvalidateOptionsMenu();
	}

}
