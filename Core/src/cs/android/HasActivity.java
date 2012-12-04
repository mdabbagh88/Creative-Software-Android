package cs.android;

import android.app.Activity;
import android.content.Context;

public interface HasActivity extends HasContext {
	Activity activity();

	@Override
	Context context();
}
