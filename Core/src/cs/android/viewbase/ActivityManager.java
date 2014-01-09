package cs.android.viewbase;

import static cs.java.lang.Lang.no;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class ActivityManager {

	protected final CSActivity activityBase;

	public ActivityManager(CSActivity activityBase) {
		this.activityBase = activityBase;
	}

	public ViewController create() {
		ViewController presenter = (ViewController) activityBase.activity().getSavedInstance();
		if (no(presenter)) return activityBase.create();
		return presenter;
	}

	public void onCreate(Bundle state) {
		getPresenter().setActivity(activityBase.activity());
		getPresenter().setView(getPresenter().asView());
		getPresenter().onBeforeCreate(state);
		activityBase.activity().setContentView(getPresenter().asView());
		getPresenter().onCreate(state);
	}

	public void onDestroy() {
		onDestroyUnbindDrawables(getPresenter().getView());
		getPresenter().onDestroy();
		System.gc();
	}

	private ViewController getPresenter() {
		return activityBase.controller();
	}

	private void onDestroyUnbindDrawables(View view) {
		if (view.getBackground() != null) view.getBackground().setCallback(null);
		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
				onDestroyUnbindDrawables(((ViewGroup) view).getChildAt(i));
			try {
				((ViewGroup) view).removeAllViews();
			} catch (Exception e) {
			}
		}
	}

}