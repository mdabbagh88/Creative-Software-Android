package cs.android;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import cs.android.viewbase.ActivityResult;
import cs.android.viewbase.IsView;
import cs.java.event.Event;
import cs.java.lang.Value;

public interface IActivityWidget extends HasActivity, IsView {
	@Override
	Activity activity();

	@Override
	View asView();

	@Override
	Context context();

	Event<ActivityResult> getOnActivityResult();

	Event<Value<Boolean>> getOnBack();

	Event<Bundle> getOnCreate();

	Event<Void> getOnDestroy();

	Event<Void> getOnPause();

	Event<Void> getOnResume();

	Event<Bundle> getOnSaveInstance();

	Event<Void> getOnStart();

	Event<Void> getOnStop();

	View getView(int viewId);

	boolean isPaused();

	boolean isResumed();

	void onBackPressed(Value<Boolean> willPressBack);

	void onDeinitialize(Bundle state);

	void onInitialize(Bundle state);
}
