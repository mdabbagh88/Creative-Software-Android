package cs.android.viewbase;

import static cs.java.lang.Lang.INVOKE_FAILED;
import static cs.java.lang.Lang.event;
import static cs.java.lang.Lang.invoke;
import static cs.java.lang.Lang.NO;
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
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuInflater;

import cs.android.lang.DoLater;
import cs.java.event.Event;
import cs.java.event.Task;
import cs.java.lang.Value;

public abstract class ViewController extends Widget<View> {

	protected Activity _activity;
	private Bundle state;
	public final Event<Void> onPause = event();
	public final Event<Bundle> onCreate = event();
	public final Event<Bundle> onBeforeCreate = event();
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
	public final Event<OnKeyDownResult> onKeyDown = event();
	private Task parentEventsTask;
	private boolean created;
	private boolean resumed;
	private boolean paused;
	private boolean destroyed;
	private final ViewController _parent;
	private int viewId;
	private LayoutId _layoutId;
	private static ViewController _root;

	public boolean hasLayout() {
		return is(_layoutId);
	}

	public ViewController(ViewController parent) {
		_parent = parent;
		listenParent();
	}

	public static ViewController root() {
		return _root;
	}

	public FragmentManager getSupportFragmentManager() {
		Object manager = invoke(activity(), "getSupportFragmentManager");
		if (manager == INVOKE_FAILED) return null;
		return (FragmentManager) manager;
	}

	public ViewController(ViewController parent, int viewId) {
		_parent = parent;
		this.viewId = viewId;
		listenParent();
	}

	@SuppressWarnings("deprecation") public int getScreenOrientation() {
		Display getOrient = activity().getWindowManager().getDefaultDisplay();
		int orientation = Configuration.ORIENTATION_UNDEFINED;
		if (getOrient.getWidth() == getOrient.getHeight()) orientation = Configuration.ORIENTATION_SQUARE;
		else if (getOrient.getWidth() < getOrient.getHeight()) orientation = Configuration.ORIENTATION_PORTRAIT;
		else orientation = Configuration.ORIENTATION_LANDSCAPE;
		return orientation;
	}

	public View findViewUp(int id) {
		View view = null;
		ViewController parent = parent();
		while (parent != null && (view = parent.getViewGroup(id)) == null)
			parent = parent().getParent();
		return view;
	}

	public ViewController(ViewController parent, LayoutId id) {
		_parent = parent;
		_layoutId = id;
		_root = null;
		listenParent();
	}

	public ViewController(LayoutId layoutId) {
		_layoutId = layoutId;
		_parent = null;
		_root = this;
		listenParent();
	}

	public Activity activity() {
		return _activity;
	}

	public View asView() {
		if (is(getView())) return getView();
		else if (set(viewId)) {
			setView(parent().asView().findViewById(viewId));
			if (no(getView())) throw unexpected("Expected", this, "in parent", parent());
		} else if (set(_layoutId)) setView(inflateLayout(_layoutId.value));
		else setView(parent().asView());
		return getView();
	}

	public Bundle getState() {
		return state;
	}

	public int getViewId() {
		return viewId;
	}

	public Context context() {
		if (is(parent())) return parent().context();
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
		new DoLater(200) {
			public void run() {
				hideSoftInput(0);
			}
		};
	}

	public void onDeinitialize(Bundle state) {
		parentEventsTask.cancel();
		onPause();
		onSaveInstanceState(state);
		onStop();
	}

	public void onInitialize(Bundle state) {
		onBeforeCreate(state);
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
		if (is(parent())) parent().goBack();
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
		this.state = state;
		fire(onCreate, state);
		onCreate();
		if (no(state)) onCreateFirstTime();
		else onCreateRestore(state);
		created = true;
		paused = NO;
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
		if (is(parent()))
			parentEventsTask = new Task(parent().onBeforeCreate, parent().onCreate, parent().onStart,
					parent().onResume, parent().onPause, parent().onStop, parent().onSaveInstance,
					parent().onDestroy, parent().onBack, parent().onActivityResult,
					parent().onCreateOptionsMenu, parent().onOptionsItemSelected,
					parent().onPrepareOptionsMenu, parent().onKeyDown) {
				@SuppressWarnings("unchecked") public void run() {
					if (event == parent().onBeforeCreate) onBeforeCreate((Bundle) argument);
					else if (event == parent().onCreate) onCreate((Bundle) argument);
					else if (event == parent().onStart) onStart();
					else if (event == parent().onResume) onResume();
					else if (event == parent().onPause) onPause();
					else if (event == parent().onStop) onStop();
					else if (event == parent().onSaveInstance) onSaveInstanceState((Bundle) argument);
					else if (event == parent().onDestroy) onDestroy();
					else if (event == parent().onBack) onBackPressed((Value<Boolean>) argument);
					else if (event == parent().onActivityResult) onActivityResult((ActivityResult) argument);
					else if (event == parent().onCreateOptionsMenu) onCreateOptionsMenu((OnMenu) argument);
					else if (event == parent().onOptionsItemSelected) onOptionsItemSelected((OnMenuItem) argument);
					else if (event == parent().onPrepareOptionsMenu) onPrepareOptionsMenu((OnMenu) argument);
					else if (event == parent().onKeyDown) onKeyDown((OnKeyDownResult) argument);
					else throw unexpected();
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

	public void onKeyDown(OnKeyDownResult onKey) {
		fire(onKeyDown, onKey);
	}

	public void onBeforeCreate(Bundle state) {
		if (is(parent())) setActivity(parent().activity());
		fire(onBeforeCreate, state);
	}

	public ViewController getParent() {
		return parent();
	}

	public ViewController parent() {
		return _parent;
	}

}
