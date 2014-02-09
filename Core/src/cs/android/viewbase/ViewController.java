package cs.android.viewbase;

import static cs.java.lang.Lang.INVOKE_FAILED;
import static cs.java.lang.Lang.NO;
import static cs.java.lang.Lang.YES;
import static cs.java.lang.Lang.event;
import static cs.java.lang.Lang.fire;
import static cs.java.lang.Lang.invoke;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.Display;
import android.view.MenuInflater;
import android.view.View;
import cs.android.view.InViewController;
import cs.java.event.Event;
import cs.java.event.Task;
import cs.java.lang.Value;

public abstract class ViewController extends Widget<View> {

	public static ViewController root() {
		return _root;
	}
	protected Activity _activity;
	private Bundle state;
	public final Event<Void> onPause = event();
	public final Event<Bundle> onCreate = event();
	public final Event<Bundle> onBeforeCreate = event();
	public final Event<Value<Boolean>> onBack = event();
	public final Event<Void> onStart = event();
	public final Event<Void> onStop = event();
	public final Event<Void> onLowMemory = event();
	public final Event<Bundle> onSaveInstance = event();
	public final Event<Void> onDestroy = event();
	public final Event<Void> onResume = event();
	public final Event<Void> onUserLeaveHint = event();
	public final Event<OnMenu> onPrepareOptionsMenu = event();
	public final Event<OnMenuItem> onOptionsItemSelected = event();
	public final Event<OnMenu> onCreateOptionsMenu = event();
	public final Event<ActivityResult> onActivityResult = event();
	public final Event<OnKeyDownResult> onKeyDown = event();
	public final Event<Intent> onNewIntent = event();
	private Task parentEventsTask;
	private boolean created;
	private boolean resumed;
	private boolean paused;
	private boolean destroyed;
	private final ViewController _parent;
	private int viewId;
	private LayoutId _layoutId;
	private boolean started;
	private InViewController _parentInView;
	private static boolean _startingActivity;
	private static ViewController _root;

	public static boolean isStartingActivity() {
		return _startingActivity;
	}

	public ViewController(InViewController parent, LayoutId id) {
		this((ViewController) parent, id);
		_parentInView = parent;
	}

	public ViewController(LayoutId layoutId) {
		_startingActivity = NO;
		_layoutId = layoutId;
		_parent = null;
		_root = this;
		listenParent();
	}

	public ViewController(ViewController parent) {
		_parent = parent;
		listenParent();
	}

	public ViewController(ViewController parent, int viewId) {
		_parent = parent;
		this.viewId = viewId;
		listenParent();
	}

	public ViewController(ViewController parent, LayoutId id) {
		_parent = parent;
		_layoutId = id;
		_root = null;
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

	public Context context() {
		if (is(parent())) return parent().context();
		return super.context();
	}

	public View findViewUp(int id) {
		View view = null;
		ViewController parent = parent();
		while (parent != null && (view = parent.getViewGroup(id)) == null)
			parent = parent().parent();
		return view;
	}

	public ActionBar getActionBar() {
		return ((CSActivity) activity()).getSupportActionBar();
	}

	public Fragment getFragment(int id) {
		return getFragmentManager().findFragmentById(id);
	}

	public FragmentManager getFragmentManager() {
		return ((CSActivity) activity()).getSupportFragmentManager();
	}

	protected Bundle getIntentExtras() {
		return activity().getIntent().getExtras();
	}

	public MenuInflater getMenuInflater() {
		return ((CSActivity) activity()).getSupportMenuInflater();
	}

	@SuppressWarnings("deprecation") public int getScreenOrientation() {
		Display getOrient = activity().getWindowManager().getDefaultDisplay();
		int orientation = Configuration.ORIENTATION_UNDEFINED;
		if (getOrient.getWidth() == getOrient.getHeight()) orientation = Configuration.ORIENTATION_SQUARE;
		else if (getOrient.getWidth() < getOrient.getHeight()) orientation = Configuration.ORIENTATION_PORTRAIT;
		else orientation = Configuration.ORIENTATION_LANDSCAPE;
		return orientation;
	}

	public Bundle getState() {
		return state;
	}

	public FragmentManager getSupportFragmentManager() {
		Object manager = invoke(activity(), "getSupportFragmentManager");
		if (manager == INVOKE_FAILED) return null;
		return (FragmentManager) manager;
	}

	public int getViewId() {
		return viewId;
	}

	public void goBack() {
		if (is(parent())) parent().goBack();
		else activity().onBackPressed();
	}

	protected void goHome() {
		startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
	}

	public boolean hasLayout() {
		return is(_layoutId);
	}

	public void hide() {
		if (is(_parentInView)) _parentInView.hideController();
		else super.hide();
	}

	public Intent intent() {
		return activity().getIntent();
	}

	public void invalidateOptionsMenu() {
		((CSActivity) activity()).supportInvalidateOptionsMenu();
	}

	public boolean isCreated() {
		return created;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	protected boolean isFirstTime() {
		return no(state);
	}

	protected boolean isMainActionRun() {
		return Intent.ACTION_MAIN.equals(activity().getIntent().getAction());
	}

	public boolean isPaused() {
		return paused;
	}

	public boolean isResumed() {
		return resumed;
	}

	public boolean isStarted() {
		return started;
	}

	public void listenParent() {
		if (is(parent()))
			parentEventsTask = new Task(parent().onBeforeCreate, parent().onCreate, parent().onStart,
					parent().onResume, parent().onPause, parent().onStop, parent().onSaveInstance,
					parent().onDestroy, parent().onBack, parent().onActivityResult,
					parent().onCreateOptionsMenu, parent().onOptionsItemSelected,
					parent().onPrepareOptionsMenu, parent().onKeyDown, parent().onNewIntent,
					parent().onUserLeaveHint, parent().onLowMemory) {
				@SuppressWarnings("unchecked") public void run() {
					if (event == parent().onBeforeCreate) onBeforeCreate((Bundle) argument);
					else if (event == parent().onCreate) onCreate((Bundle) argument);
					else if (event == parent().onStart) onStart();
					else if (event == parent().onResume) onResume();
					else if (event == parent().onPause) onPause();
					else if (event == parent().onStop) onStop();
					else if (event == parent().onLowMemory) onLowMemory();
					else if (event == parent().onSaveInstance) onSaveInstanceState((Bundle) argument);
					else if (event == parent().onDestroy) onDestroy();
					else if (event == parent().onBack) onBackPressed((Value<Boolean>) argument);
					else if (event == parent().onActivityResult) onActivityResult((ActivityResult) argument);
					else if (event == parent().onCreateOptionsMenu) onCreateOptionsMenu((OnMenu) argument);
					else if (event == parent().onOptionsItemSelected) onOptionsItemSelected((OnMenuItem) argument);
					else if (event == parent().onPrepareOptionsMenu) onPrepareOptionsMenu((OnMenu) argument);
					else if (event == parent().onKeyDown) onKeyDown((OnKeyDownResult) argument);
					else if (event == parent().onNewIntent) onNewIntent((Intent) argument);
					else if (event == parent().onUserLeaveHint) onUserLeaveHint();
					else throw unexpected();
				}
			};
	}

	protected void onActivityResult(ActivityResult result) {
		fire(onActivityResult, result);
	}

	public void onBackPressed(Value<Boolean> goBack) {
		fire(onBack, goBack);
		if (goBack.get()) goBack.set(onGoBack());
	}

	public void onBeforeCreate(Bundle state) {
		if (is(parent())) setActivity(parent().activity());
		fire(onBeforeCreate, state);
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

	public void onCreateOptionsMenu(OnMenu menu) {
		fire(onCreateOptionsMenu, menu);
	}

	/**
	 * initialize view
	 */
	protected void onCreateRestore(Bundle state) {
	}

	public void onDeinitialize(Bundle state) {
		parentEventsTask.cancel();
		onPause();
		onSaveInstanceState(state);
		onStop();
	}

	public void onDestroy() {
		destroyed = true;
		setActivity(null);
		setView(null);
		fire(onDestroy);
	}

	protected Boolean onGoBack() {
		return YES;
	}

	public void onInitialize(Bundle state) {
		onBeforeCreate(state);
		onCreate(state);
		onStart();
		onResume();
	}

	public void onKeyDown(OnKeyDownResult onKey) {
		fire(onKeyDown, onKey);
	}

	protected void onLowMemory() {
		fire(onLowMemory);
	}

	public void onNewIntent(Intent intent) {
		fire(onNewIntent, intent);
	}

	public void onOptionsItemSelected(OnMenuItem item) {
		fire(onOptionsItemSelected, item);
	}

	protected void onPause() {
		resumed = false;
		paused = true;
		fire(onPause);
	}

	public void onPrepareOptionsMenu(OnMenu menu) {
		fire(onPrepareOptionsMenu, menu);
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
		started = true;
		fire(onStart);
	}

	protected void onStop() {
		started = false;
		state = null;
		fire(onStop);
	}

	protected void onUserLeaveHint() {
		fire(onUserLeaveHint);
	}

	protected void overridePendingTransition(int in, int out) {
		activity().overridePendingTransition(in, out);
	}

	public ViewController parent() {
		return _parent;
	}

	public void removeFragment(int id) {
		Fragment fragment = getFragment(id);
		if (fragment != null) getFragmentManager().beginTransaction().remove(fragment).commit();
	}

	protected void setActivity(Activity activity) {
		if (paused) setView(null);
		_activity = activity;
		setContext(activity);
	}

	protected void setContentView(int layoutResId) {
		activity().setContentView(layoutResId);
	}

	public void show() {
		if (is(_parentInView)) _parentInView.showController(this);
		else super.show();
	}

	public void startActivity(Class<? extends Activity> activityClass) {
		startActivity(new Intent(activity(), activityClass));
	}

	public void startActivity(Intent intent) {
		_startingActivity = YES;
		activity().startActivity(intent);
	}

	public void startActivityForResult(Class<? extends Activity> activityClass, int requestCode) {
		startActivityForResult(new Intent(activity(), activityClass), requestCode);
	}

	public void startActivityForResult(Intent intent, int requestCode) {
		_startingActivity = YES;
		activity().startActivityForResult(intent, requestCode);
	}

	public void switchActivity(Class<? extends Activity> activityClass) {
		switchActivity(new Intent(activity(), activityClass));
	}

	public void switchActivity(Class<? extends Activity> activityClass, int resultCode) {
		activity().setResult(resultCode);
		switchActivity(new Intent(activity(), activityClass));
	}

	public void switchActivity(Class<? extends Activity> activityClass, String key, Serializable value) {
		Intent intent = new Intent(activity(), activityClass);
		intent.putExtra(key, value);
		switchActivity(intent);
	}

	public void switchActivity(Intent intent) {
		startActivity(intent);
		activity().finish();
	}

	public void onHideFromInView() {
	}

	public void onInViewHide(ViewController controller) {
	}

}
