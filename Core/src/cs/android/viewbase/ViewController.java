package cs.android.viewbase;

import static cs.android.lang.AndroidLang.event;
import static cs.java.lang.Lang.No;
import static cs.java.lang.Lang.fire;
import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.no;
import static cs.java.lang.Lang.set;
import static cs.java.lang.Lang.unexpected;

import java.io.Serializable;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import cs.android.IActivityWidget;
import cs.java.event.Event;
import cs.java.event.Task;
import cs.java.lang.Value;

public abstract class ViewController extends Widget<View> implements IActivityWidget {

	protected Activity activity;
	private Bundle state;
	private final Event<Void> eventPause = event();
	private final Event<Bundle> eventCreate = event();
	private final Event<Value<Boolean>> onBack = event();
	private final Event<Void> eventStart = event();
	private final Event<Void> eventStop = event();
	private final Event<Bundle> eventSaveInstance = event();
	private final Event<Void> eventDestroy = event();
	private final Event<Void> eventResume = event();
	private final Event<ActivityResult> eventActivityResult = event();
	private Task parentEventsTask;
	private boolean created;
	private boolean resumed;
	private boolean paused;
	private boolean destroyed;
	protected IActivityWidget parent;
	private int viewId;
	private LayoutId layoutId;
	public static ViewController root;
	private Dialog _dialog;

	public ViewController(IActivityWidget parent) {
		this.parent = parent;
		listenParent();
	}

	public void setDialog(Dialog dialog) {
		_dialog = dialog;
	}

	public ViewController(IActivityWidget parent, int viewId) {
		this.parent = parent;
		this.viewId = viewId;
		listenParent();
	}

	public ViewController(IActivityWidget parent, LayoutId id) {
		this.parent = parent;
		layoutId = id;
		listenParent();
	}

	public ViewController(LayoutId layoutId) {
		this.layoutId = layoutId;
		root = this;
		listenParent();
	}

	@Override public Activity activity() {
		return activity;
	}

	@Override public View asView() {
		if (is(getView()))
			return getView();
		else if (set(viewId)) {
			setView(parent.asView().findViewById(viewId));
			if (no(getView())) throw unexpected("Expected", this, "in parent", parent);
		} else if (set(layoutId))
			setView(inflateLayout(layoutId.value));
		else setView(parent.asView());
		return getView();
	}

	@Override public Event<ActivityResult> getOnActivityResult() {
		return eventActivityResult;
	}

	@Override public Event<Value<Boolean>> getOnBack() {
		return onBack;
	}

	@Override public Event<Bundle> getOnCreate() {
		return eventCreate;
	}

	@Override public Event<Void> getOnDestroy() {
		return eventDestroy;
	}

	@Override public Event<Void> getOnPause() {
		return eventPause;
	}

	@Override public Event<Void> getOnResume() {
		return eventResume;
	}

	@Override public Event<Bundle> getOnSaveInstance() {
		return eventSaveInstance;
	}

	@Override public Event<Void> getOnStart() {
		return eventStart;
	}

	@Override public Event<Void> getOnStop() {
		return eventStop;
	}

	public Bundle getState() {
		return state;
	}

	public int getViewId() {
		return viewId;
	}

	@Override public Context context() {
		if (is(parent)) return parent.context();
		return super.context();
	}

	public boolean isCreated() {
		return created;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	@Override public boolean isPaused() {
		return paused;
	}

	@Override public boolean isResumed() {
		return resumed;
	}

	@Override public void onBackPressed(Value<Boolean> goBack) {
		fire(onBack, goBack);
	}

	@Override public void onDeinitialize(Bundle state) {
		onPause();
		onSaveInstanceState(state);
		onStop();
		onDestroy();
		parentEventsTask.cancel();
	}

	@Override public void onInitialize(Bundle state) {
		onCreate(state);
		onStart();
		onResume();
	}

	void setActivity(Activity activity) {
		if (paused) setView(null);
		this.activity = activity;
		setContext(activity);
	}

	protected Bundle getIntentExtras() {
		return activity().getIntent().getExtras();
	}

	protected PackageInfo getPackageInfo() {
		try {
			return activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			return null;
		}
	}

	public void goBack() {
		if (is(_dialog))
			_dialog.onBackPressed();
		else if (is(parent))
			parent.goBack();
		else activity().onBackPressed();
	}

	protected boolean isFirstTime() {
		return no(state);
	}

	protected boolean isMainActionRun() {
		return Intent.ACTION_MAIN.equals(activity().getIntent().getAction());
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		fire(eventActivityResult, new ActivityResult(requestCode, resultCode, data));
	}

	/**
	 * initialize view
	 */
	protected void onCreate() {
	}

	protected void onCreate(Bundle state) {
		if (is(parent)) setActivity(parent.activity());
		this.state = state;
		fire(eventCreate, state);
		onCreate();
		if (no(state))
			onCreateFirstTime();
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

	protected void onDestroy() {
		destroyed = true;
		setActivity(null);
		setView(null);
		fire(eventDestroy);
	}

	protected void onPause() {
		resumed = false;
		paused = true;
		fire(eventPause);
	}

	/**
	 * update view
	 */
	protected void onResume() {
		resumed = true;
		paused = false;
		fire(eventResume);
	}

	protected void onSaveInstanceState(Bundle state) {
		fire(eventSaveInstance, state);
	}

	protected void onStart() {
		resumed = true;
		paused = false;
		fire(eventStart);
	}

	protected void onStop() {
		resumed = false;
		state = null;
		fire(eventStop);
	}

	protected void overridePendingTransition(int in, int out) {
		activity().overridePendingTransition(in, out);
	}

	protected void setContentView(int layoutResId) {
		activity().setContentView(layoutResId);
	}

	protected void startActivity(Class<? extends Activity> activityClass) {
		startActivity(new Intent(activity(), activityClass));
	}

	protected void startActivity(Intent intent) {
		activity().startActivity(intent);
	}

	protected void startActivityForResult(Class<? extends Activity> activityClass, int requestCode) {
		startActivityForResult(new Intent(activity(), activityClass), requestCode);
	}

	protected void startActivityForResult(Intent intent, int requestCode) {
		activity().startActivityForResult(intent, requestCode);
	}

	protected void switchActivity(Class<? extends Activity> activityClass) {
		switchActivity(new Intent(activity(), activityClass));
	}

	protected void switchActivity(Class<? extends Activity> activityClass, String key,
			Serializable value) {
		Intent intent = new Intent(activity(), activityClass);
		intent.putExtra(key, value);
		switchActivity(intent);
	}

	protected void switchActivity(Class<? extends Activity> activityClass, int resultCode) {
		activity().setResult(resultCode);
		switchActivity(new Intent(activity(), activityClass));
	}

	protected void switchActivity(Intent intent) {
		startActivity(intent);
		activity().finish();
	}

	public void listenParent() {
		if (is(parent))
			parentEventsTask = new Task(parent.getOnCreate(), parent.getOnStart(), parent.getOnResume(),
					parent.getOnPause(), parent.getOnStop(), parent.getOnSaveInstance(),
					parent.getOnDestroy(), parent.getOnBack(), parent.getOnActivityResult()) {
				@Override @SuppressWarnings("unchecked") public void run() {
					if (event == parent.getOnCreate()) onCreate((Bundle) argument);
					if (event == parent.getOnStart()) onStart();
					if (event == parent.getOnResume()) onResume();
					if (event == parent.getOnPause()) onPause();
					if (event == parent.getOnStop()) onStop();
					if (event == parent.getOnSaveInstance()) onSaveInstanceState((Bundle) argument);
					if (event == parent.getOnDestroy()) onDestroy();
					if (event == parent.getOnBack()) onBackPressed((Value<Boolean>) argument);
					if (event == parent.getOnActivityResult()) {
						ActivityResult result = (ActivityResult) argument;
						onActivityResult(result.requestCode, result.resultCode, result.data);
					}
				}
			};
	}
}
