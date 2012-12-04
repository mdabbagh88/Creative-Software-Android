package cs.android.viewbase;

import static cs.java.lang.Lang.no;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class ActivityManager {

	protected final IsActivityBase activityBase;

	public ActivityManager(IsActivityBase activityBase) {
		this.activityBase = activityBase;
	}

	public ActivityWidget create() {
		ActivityWidget presenter = (ActivityWidget) activityBase.activity()
				.getLastNonConfigurationInstance();
		if (no(presenter)) return activityBase.create();
		return presenter;
	}

	public void onCreate(Bundle state) {
		getPresenter().setActivity(activityBase.activity());
		getPresenter().setView(getPresenter().asView());
		activityBase.activity().setContentView(getPresenter().asView());
		getPresenter().onCreate(state);
	}

	public void onDestroy() {
		onDestroyUnbindDrawables(getPresenter().getView());
		getPresenter().onDestroy();
		System.gc();
	}

	private ActivityWidget getPresenter() {
		return activityBase.getPresenter();
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